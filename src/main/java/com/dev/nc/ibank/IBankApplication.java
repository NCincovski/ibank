package com.dev.nc.ibank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "iBank API", version = "1.0", description = "Documentation iBank API v1.0"))
@SpringBootApplication
public class IBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(IBankApplication.class, args);
    }

}
