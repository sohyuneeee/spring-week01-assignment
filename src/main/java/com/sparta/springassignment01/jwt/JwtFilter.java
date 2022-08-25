package com.sparta.springassignment01.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springassignment01.dto.ResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORITIES_KEY = "auth";

    private final String SECRET_KEY;

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Claims claims;
            try {
                claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
            } catch (ExpiredJwtException e) {
                claims = e.getClaims();
            }

            if (claims.getExpiration().toInstant().toEpochMilli() < Instant.now().toEpochMilli()) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                ResponseDto.fail("BAD_REQUEST", "Token이 유효햐지 않습니다.")
                        )
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            String subject = claims.getSubject();
            UserDetails principal = userDetailsService.loadUserByUsername(subject);

            // DB의 member의 role을 가져옴
            Collection<? extends GrantedAuthority> repoMemberRole = principal.getAuthorities();

            // jwt의 role을 가져옴
            String jwtRole = claims.get(AUTHORITIES_KEY).toString();

            boolean checkRole = false;
            for (GrantedAuthority role : repoMemberRole) {
                if (jwtRole.equals(role.getAuthority())) {
                    checkRole = true;
                }
            }

            // db의 role과 jwt의 role 일치하지 않을 때
            if (!checkRole) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                ResponseDto.fail("BAD_REQUEST","Token이 유효햐지 않습니다.")
                        )
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(jwtRole);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response); // 시큐리티 체인이기 때문에 해당 메소드 종료 시에는 이 명령어를 사용해야한다.

    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
