package com.example.githubservice.domain

data class RepositoryWithBranches(
        val repository: String,
        val branches: List<Branch>
)

data class Branch(
        val name: String,
        val commitSha: String
)