package com.example.githubservice.ports

data class Repository(val name: String, val user: String, val isForked: Boolean)

data class Branch(val name: String, val commitSha: String)

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