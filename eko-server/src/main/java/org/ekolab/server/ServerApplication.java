package org.ekolab.server;

import org.aderevyanko.restorator.entity.BaseEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackageClasses = ServerApplication.class)
@EntityScan(basePackageClasses = BaseEntity.class)
public class ServerApplication {
    public static void main(String... args) {
        ApplicationContext ctx = SpringApplication.run(ServerApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}