package com.example.githubservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class BeanConfiguration() {

    @Bean
    fun webClient(): WebClient.Builder {
        return WebClient.builder()
    }
}