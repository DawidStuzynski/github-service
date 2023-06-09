package com.example.githubservice.domain

data class Repository(val name: String, val user: String, val isForked: Boolean)

data class RepositoryResponse(
        val name: String,
        val owner: Owner,
        val fork: Boolean
)

data class Owner(
        val login: String
)

data class BranchResponse(
        val name: String,
        val commit: Commit,
)

data class Commit(
        val sha: String,
)