package com.example.githubservice.dto

data class RepositoryWithBranchesDto(
        val repository: String,
        val branches: List<BranchDto>
)

data class BranchDto(
        val name: String,
        val commitSha: String
)