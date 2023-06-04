package com.example.githubservice.controller

import com.example.githubservice.model.GitHubRepository
import com.example.githubservice.service.RepositoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RepositoryController(private val service: RepositoryService) {

    @GetMapping("/{username}")
    fun getRepositories(@PathVariable username: String, @RequestHeader("Accept") acceptHeader: String?): ResponseEntity<List<GitHubRepository>> {

        if (acceptHeader != null && acceptHeader == "application/xml") {
            throw NotAcceptableHeaderException("application/xml is not supported.")
        } else {
            val repositories = service.getRepositories(username)
            return ResponseEntity.accepted().body(repositories)
        }
    }
}