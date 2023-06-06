package com.example.githubservice.configuration

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.TimeUnit


@Configuration
class BeanConfiguration(val configuration: RepositoryConfiguration) {
    @Bean
    fun okhttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .callTimeout(configuration.getOkhttpTimeout(), TimeUnit.MILLISECONDS)
                .build()
    }

    @Bean
    fun webClient(): WebClient {
        return WebClient.create()
    }
}