package com.example.familyhealth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.familyhealth.mapper")
public class FamilyHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyHealthApplication.class, args);
    }

}
