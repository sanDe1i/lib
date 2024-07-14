package com.example.lm;

import com.example.lm.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class LmApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmApplication.class, args);
    }

}
