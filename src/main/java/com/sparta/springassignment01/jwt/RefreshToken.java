package com.sparta.springassignment01.jwt;

import com.sparta.springassignment01.entity.Member;
import com.sparta.springassignment01.entity.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken extends Timestamped {

    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name ="token_value", nullable = false)
    private String value;

    public void updateValue(String token) {
        this.value = token;
    }
}

