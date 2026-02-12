package com.example.blog.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionManagement {

    @Bean
       public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
           return new JpaTransactionManager(emf);
       }
    }



