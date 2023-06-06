package com.example.githubservice.adapter

import com.example.githubservice.configuration.RepositoryConfiguration
import com.example.githubservice.exception.InternalServerErrorException
import com.example.githubservice.exception.RepositoryNotFoundException
import com.example.githubservice.exception.UserNotFoundException
import com.example.githubservice.ports.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class RepositoryAdapterAPIv3(val webClient: WebClient, val objectMapper: ObjectMapper, val configuration: RepositoryConfiguration) : RepositoryPort {

    companion object {
        const val INTERNAL_ERROR_MESSAGE = "Internal server error occurred"
    }


    override fun getRepositoriesByName(username: String): List<Repository> {

        val url = "https://api.github.com/users/$username/repos"

        val response = webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String::class.java)
                .block() ?: throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)

        val responseBody = response.toString()
        val statusCode = response.statusCode

        if (!statusCode.is2xxSuccessful) {
            when (statusCode) {
                HttpStatusCode.valueOf(404) -> {
                    val errorMessage = "User with name $username not found"
                    throw UserNotFoundException(errorMessage)
                }

                else -> {
                    throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)
                }
            }
        }
        val responseList: List<RepositoryResponse>
        try {
            responseList = objectMapper.readValue(responseBody,
                    objectMapper.typeFactory.constructCollectionType(List::class.java, RepositoryResponse::class.java))
        } catch (ex: Exception) {
            throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)
        }


        val repositories: List<Repository> = responseList.map {
            Repository(
                    name = it.name,
                    user = it.owner.login,
                    isForked = it.fork
            )
        }
        return repositories
    }

    override fun getBranchesByUsernameAndRepository(username: String, repository: String): List<Branch> {
        val url = "https://api.github.com/repos/$username/$repository/branches"

        val response = webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String::class.java)
                .block() ?: throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)

        val responseBody = response.toString()
        val statusCode = response.statusCode
        if (!statusCode.is2xxSuccessful) {
            when (statusCode) {
                HttpStatusCode.valueOf(404) -> {
                    val errorMessage = "Repository with name $repository not found"
                    throw RepositoryNotFoundException(errorMessage)
                }

                else -> {
                    throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)
                }
            }
        }

        val responseList: List<BranchResponse>
        try {
            responseList = objectMapper.readValue(responseBody,
                    objectMapper.typeFactory.constructCollectionType(List::class.java, BranchResponse::class.java))
        } catch (ex: Exception) {
            throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)
        }

        val branches: List<Branch> = responseList.map {
            Branch(
                    name = it.name,
                    commitSha = it.commit.sha
            )
        }
        return branches
    }
}