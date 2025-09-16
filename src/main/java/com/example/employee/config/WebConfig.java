package com.example.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                for (HttpMessageConverter<?> converter : converters) {
                    if (converter instanceof MappingJackson2HttpMessageConverter) {
                        List<MediaType> types = new ArrayList<>(converter.getSupportedMediaTypes());
                        types.add(MediaType.valueOf("application/json;charset=UTF-8"));
                        ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(types);
                    }
                }
            }
        };
    }
}
