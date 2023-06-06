package com.example.githubservice.controller

import com.example.githubservice.dto.RepositoryWithBranchesDTO
import com.example.githubservice.exception.NotAcceptableHeaderException
import com.example.githubservice.mappers.DomainToDtoMapper
import com.example.githubservice.services.RepositoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RepositoryController(private val service: RepositoryService, val mapper: DomainToDtoMapper) {

    @GetMapping("/repository-with-branches/{username}")
    fun getRepositories(@PathVariable username: String, @RequestHeader("Accept") acceptHeader: String?): List<RepositoryWithBranchesDTO> {

        if (acceptHeader != null && acceptHeader == "application/xml") {
            throw NotAcceptableHeaderException("application/xml is not supported.")
        } else {
            return service.getRepositoryBranchesByUsername(username)
                    .map { mapper.toDto(it) }
        }
    }
}