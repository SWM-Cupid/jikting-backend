package com.cupid.jikting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class JiktingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiktingBackendApplication.class, args);
    }
}
