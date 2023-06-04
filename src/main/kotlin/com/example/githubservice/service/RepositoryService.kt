package com.example.githubservice.service

import com.example.githubservice.model.Branch
import com.example.githubservice.model.GitHubRepository
import com.example.githubservice.model.Owner
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.springframework.stereotype.Service

@Service
class RepositoryService {

    private val httpClient = OkHttpClient()

    fun getRepositories(username: String): List<GitHubRepository> {

        val repositoriesList = getRepositoriesByUsername(username)
        return getRepositoriesData(repositoriesList)
    }

    private fun getRepositoriesData(repositoriesList: JSONArray): MutableList<GitHubRepository> {
        val result = mutableListOf<GitHubRepository>()

        for (i in 0 until repositoriesList.length()) {
            val repositoryJson = repositoriesList.getJSONObject(i)
            val isForked = repositoryJson.getBoolean("fork")

            if (!isForked) {
                val name = repositoryJson.getString("name")
                val owner = repositoryJson.getJSONObject("owner").getString("login")
                val branchesUrl = repositoryJson.getString("branches_url").removeSuffix("{/branch}")
                val branchList = getBranchesByRepoUrl(branchesUrl)

                if (!name.isNullOrBlank() && !owner.isNullOrBlank() && branchList.isNotEmpty()) {
                    result.add(GitHubRepository(name, Owner(owner), branchList))
                }
            }
        }
        return result
    }

    private fun getRepositoriesByUsername(username: String): JSONArray {
        val url = "https://api.github.com/users/$username/repos"

        val request: Request = Request.Builder()
                .url(url)
                .build()

        val response: Response = httpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful) {
            return JSONArray(responseBody)
        } else {
            throw UserNotFoundException("User with name $username do not exits")
        }
    }

    private fun getBranchesByRepoUrl(branchesUrl: String): List<Branch> {
        val branchesRequest = Request.Builder()
                .url(branchesUrl)
                .build()

        val branchesResponse: Response = httpClient.newCall(branchesRequest).execute()
        val branchesResponseBody: String? = branchesResponse.body?.string()
        val branches = JSONArray(branchesResponseBody)

        val branchList = mutableListOf<Branch>()

        for (i in 0 until branches.length()) {
            val branchJson = branches.getJSONObject(i)
            val branchName = branchJson.getString("name")
            val lastCommitSha = branchJson.getJSONObject("commit").getString("sha")

            if (!branchName.isNullOrBlank() && !lastCommitSha.isNullOrBlank()) {
                branchList.add(Branch(branchName, lastCommitSha))
            }
        }
        return branchList
    }
}