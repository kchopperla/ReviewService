package com.intuit.reviewengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

@Configuration
public class PropertySourcesConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        PropertySource applicationProperties = applicationProperties();
        if (applicationProperties != null) {
            MutablePropertySources propertySources = new MutablePropertySources();
            propertySources.addFirst(applicationProperties);
            configurer.setPropertySources(propertySources);
        }
        return configurer;
    }

    public static PropertySource applicationProperties() {
        try {
            return new ResourcePropertySource("classpath:application.properties");
        } catch (IOException e) {
            return null;
        }
    }

}
