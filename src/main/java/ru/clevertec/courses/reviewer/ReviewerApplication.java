package ru.clevertec.courses.reviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableFeignClients
@SpringBootApplication
@EnableAspectJAutoProxy
public class ReviewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewerApplication.class, args);
    }

}
