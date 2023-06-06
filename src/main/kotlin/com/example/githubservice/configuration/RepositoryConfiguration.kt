package com.example.githubservice.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration() {
    @Value("\${github.api.url}")
    private val githubUrl: String = ""

    fun getGithubUrl(): String {
        return githubUrl
    }

    @Value("\${okhttp.timeout}")
    private val okhttpTimeout: Long = 5000

    fun getOkhttpTimeout(): Long {
        return okhttpTimeout
    }
}