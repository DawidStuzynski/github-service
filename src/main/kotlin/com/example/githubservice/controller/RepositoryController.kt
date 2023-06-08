package com.example.githubservice.controller

import com.example.githubservice.dto.RepositoryWithBranchesDTO
import com.example.githubservice.mappers.DomainToDtoMapper
import com.example.githubservice.services.RepositoryService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RepositoryController(private val service: RepositoryService, val mapper: DomainToDtoMapper) {

    @GetMapping(value = ["repository-with-branches/{username}"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getRepositories(@PathVariable username: String, @RequestHeader("Accept") acceptHeader: String?): List<RepositoryWithBranchesDTO> {
        return service.getRepositoryBranchesByUsername(username)
                .map { mapper.toDto(it) }
    }
}