package com.example.githubservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GithubServiceApplication

fun main(args: Array<String>) {
    runApplication<GithubServiceApplication>(*args)
}
