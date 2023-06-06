package com.example.githubservice.services

import com.example.githubservice.domain.BranchResponse
import com.example.githubservice.domain.Repository
import com.example.githubservice.domain.RepositoryResponse
import com.example.githubservice.domain.Branch
import com.example.githubservice.domain.RepositoryWithBranches
import com.example.githubservice.exception.InternalServerErrorException
import com.example.githubservice.exception.RepositoryNotFoundException
import com.example.githubservice.exception.UserNotFoundException
import org.apache.http.client.utils.URIBuilder
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RepositoryService(val webClientBuilder: WebClient.Builder) {

    companion object {
        const val INTERNAL_ERROR_MESSAGE = "Internal server error occurred"
    }


    fun getRepositoryBranchesByUsername(username: String): List<RepositoryWithBranches> =
            getRepositoriesByName(username)
                    .filter { !it.isForked }
                    .map { it -> RepositoryWithBranches(it.name, getBranchesByUsernameAndRepository(username, it.name).map { Branch(it.name, it.commitSha) }) }


    fun getRepositoriesByName(username: String): List<Repository> {

        val uri = URIBuilder("https://api.github.com")
                .setPath("/users/$username/repos")
                .build()

        val response = webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .toEntityList(RepositoryResponse::class.java)
                .log()
                .onErrorMap { error -> RepositoryNotFoundException(error.message.toString()) }
                .block() ?: throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)

        val responseBody = response.body
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


        val repositories: List<Repository> = responseBody?.map {
            Repository(
                    name = it.name,
                    user = it.owner.login,
                    isForked = it.fork
            )
        } ?: throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)
        return repositories
    }


    fun getBranchesByUsernameAndRepository(username: String, repository: String): List<Branch> {

        val uri = URIBuilder("https://api.github.com")
                .setPath("/repos/$username/$repository/branches")
                .build()


        val response = webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .toEntityList(BranchResponse::class.java)
                .log()
                .onErrorMap { error -> RepositoryNotFoundException(error.message.toString()) }
                .block() ?: throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)


        val responseBody = response.body
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

        val branches: List<Branch> = responseBody?.map {
            Branch(
                    name = it.name,
                    commitSha = it.commit.sha
            )
        } ?: throw InternalServerErrorException(INTERNAL_ERROR_MESSAGE)
        return branches
    }


}

