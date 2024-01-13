package io.vinicius.umd.downloader.reddit

import io.ktor.http.Url
import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Response

internal class Reddit : Downloader {
    private val api = RedditApi()

    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response {
        val sourceType = getSourceType(url)

        val media = when(sourceType) {
            is SourceType.User -> queryUserMedia(sourceType.name, limit, extensions)
            is SourceType.Subreddit -> queryUserMedia(sourceType.name, limit, extensions)
            else -> emptyList()
        }

        return RedditResponse(url, media.toTypedArray(), source = sourceType)
    }

    fun getSourceType(url: String): SourceType {
        val regexUser = """/(?:user|u)/([^/?]+)""".toRegex()
        val regexSubreddit = """/r/([^/?]+)""".toRegex()

        return when {
            url.contains(regexUser) -> {
                val match = regexUser.find(url)
                SourceType.User(match?.groupValues?.get(1).orEmpty())
            }

            url.contains(regexSubreddit) -> {
                val match = regexSubreddit.find(url)
                SourceType.Subreddit(match?.groupValues?.get(1).orEmpty())
            }

            else -> SourceType.Unknown
        }
    }

    // region - Private methods
    private suspend fun queryUserMedia(name: String, limit: Int, extensions: List<String>): List<Media> {
        val submissions = fetchSubmissions(name, limit)
        println(submissions)
        return emptyList()
    }

    private suspend fun fetchSubmissions(name: String, limit: Int): List<Child> {
        val submissions = mutableSetOf<Child>()
        var after: String? = ""

        do {
            val response = api.getUserSubmissions(name, after.orEmpty(), 100)
            after = response.data.after
            submissions.addAll(response.data.children)
        } while (response.data.children.isNotEmpty() && submissions.size < limit && after != null)

        return submissions.sortedBy { it.data.created }
    }
    // endregion

    companion object {
        fun isMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("reddit.com", true)
        }
    }
}