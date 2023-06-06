package com.example.githubservice.dto

data class RepositoryWithBranchesDTO(
        val repository: String,
        val branches: List<BranchDTO>
)

data class BranchDTO(
        val name: String,
        val commitSha: String
)