package com.example.githubservice.ports

interface RepositoryPort {
    fun getRepositoriesByName(username: String): List<Repository>

    fun getBranchesByUsernameAndRepository(username: String, repository: String): List<Branch>
}