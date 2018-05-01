package com.github.konradr25.gis;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public AlgoService algoService() {
        return new AlgoService();
    }

    @Bean
    public FileValidatorService fileValidatorService() {
        return new FileValidatorService();
    }

    @Bean
    public FileService fileService(FileValidatorService fileValidatorService) {
        return new FileService(fileValidatorService);
    }


}
