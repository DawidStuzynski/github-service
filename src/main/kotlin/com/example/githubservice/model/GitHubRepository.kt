package com.example.githubservice.model

data class GitHubRepository(val name: String, val owner: Owner, val branches: List<Branch>)
