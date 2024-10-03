package com.ust.securerestapi;

import io.jsonwebtoken.Jwts;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.xml.bind.DatatypeConverter;

import javax.crypto.SecretKey;

@SpringBootApplication
public class SecureRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureRestApiApplication.class, args);
    }

//    @Bean
    CommandLineRunner init() {
        return args -> {
            SecretKey secretKey = Jwts.SIG.HS256.key().build();
            System.out.println("Secret key: " + DatatypeConverter.printHexBinary(secretKey.getEncoded()));
        };
    }

}
