package com.example.githubservice.mappers

import com.example.githubservice.domain.Branch
import com.example.githubservice.domain.RepositoryWithBranches
import com.example.githubservice.dto.BranchDTO
import com.example.githubservice.dto.RepositoryWithBranchesDTO
import org.springframework.stereotype.Component

@Component
class DomainToDtoMapper {
    private fun branchDomainToDto(branches: List<Branch>): List<BranchDTO> {
        return branches.map { BranchDTO(it.name, it.commitSha) }
    }

    fun toDto(it: RepositoryWithBranches): RepositoryWithBranchesDTO {
        return RepositoryWithBranchesDTO(it.repository, branchDomainToDto(it.branches))
    }
}