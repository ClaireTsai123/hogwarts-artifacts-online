package com.example.hogwartsartifactsonline;

import com.example.hogwartsartifactsonline.utils.IdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
    }

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator(1, 1);
    }

}
