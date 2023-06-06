package com.example.githubservice.services

import com.example.githubservice.domain.Branch
import com.example.githubservice.domain.Repository

interface RepositoryPort {
    fun getRepositoriesByName(username: String): List<Repository>

    fun getBranchesByUsernameAndRepository(username: String, repository: String): List<Branch>
}