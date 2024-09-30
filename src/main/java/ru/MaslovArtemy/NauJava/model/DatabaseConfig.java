package ru.MaslovArtemy.NauJava.model;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

@Configuration
public class DatabaseConfig {
    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public HashMap<Long, Transaction> transactionContainer()
    {
        return new HashMap<>();
    }
}
