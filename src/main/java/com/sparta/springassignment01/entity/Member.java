package com.sparta.springassignment01.entity;

import com.sparta.springassignment01.service.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Table(name = "member")
@Entity
public class Member extends Timestamped{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String password;


    @Builder
    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

//    //jpa 영속화 관련,,, equals 와 hashcode 메소드 재정의 해준 것
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
//            return false;
//        }
//        Member member = (Member) o;
//        return id != null && Objects.equals(id, member.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

}