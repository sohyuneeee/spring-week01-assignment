package com.sparta.springassignment01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 생성시간이 바꼈을때 자동으로 업뎃
public class SpringAssignment01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringAssignment01Application.class, args);
    }

}
