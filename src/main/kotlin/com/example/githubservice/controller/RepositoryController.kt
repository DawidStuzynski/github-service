package com.example.githubservice.controller

import com.example.githubservice.dto.RepositoryWithBranchesDto
import com.example.githubservice.exception.NotAcceptableHeaderException
import com.example.githubservice.service.RepositoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RepositoryController(private val service: RepositoryService) {

    @GetMapping("/repository-with-branches/{username}")
    fun getRepositories(@PathVariable username: String, @RequestHeader("Accept") acceptHeader: String?): List<RepositoryWithBranchesDto> {

        if (acceptHeader != null && acceptHeader == "application/xml") {
            throw NotAcceptableHeaderException("application/xml is not supported.")
        } else {
            return service.getRepositoryBranchesByUsername(username)
        }
    }
}