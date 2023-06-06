package com.example.githubservice.service

import com.example.githubservice.dto.BranchDto
import com.example.githubservice.dto.RepositoryWithBranchesDto
import com.example.githubservice.ports.RepositoryPort
import org.springframework.stereotype.Service

@Service
class RepositoryService(val repository: RepositoryPort) {

    fun getRepositoryBranchesByUsername(username: String): List<RepositoryWithBranchesDto> =
            repository.getRepositoriesByName(username)
                    .filter { !it.isForked }
                    .map { it -> RepositoryWithBranchesDto(it.name, repository.getBranchesByUsernameAndRepository(username, it.name).map { BranchDto(it.name, it.commitSha) }) }
}

