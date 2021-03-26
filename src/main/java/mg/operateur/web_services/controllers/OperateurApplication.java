package mg.operateur.web_services.controllers;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication()


@ComponentScan("mg.operateur.web_services.controllers") //to scan packages mentioned
@EnableMongoRepositories("mg.operateur.web_services.controllers")
public class OperateurApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperateurApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Bienvenue");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                // System.out.println(beanName);
            }

        };
    }

}
