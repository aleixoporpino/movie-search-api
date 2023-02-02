package com.porpapps.moviesearchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MovieSearchApiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieSearchApiServiceApplication.class, args);
    }

}
