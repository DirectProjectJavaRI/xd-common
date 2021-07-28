package org.nhindirect.xd.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@ComponentScan({"org.nhindirect.config", "org.nhind.config", "org.nhindirect.xd.common"})
@EnableFeignClients({"org.nhind.config.rest.feign"})
@EnableR2dbcRepositories("org.nhindirect.config.repository")
public class TestApplication
{	
    public static void main(String[] args) 
    {
        SpringApplication.run(TestApplication.class, args);
    }  
}
