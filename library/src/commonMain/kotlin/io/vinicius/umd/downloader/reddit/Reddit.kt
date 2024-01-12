package io.vinicius.umd.downloader.reddit

import io.ktor.http.Url
import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.downloader.reddit.model.RedditMetadata
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Metadata

class Reddit : Downloader {
    private val api = RedditApi()

    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Metadata {
        val sourceType = getSourceType(url)

        val media = when(sourceType) {
            is SourceType.User -> queryUserMedia(sourceType.name, limit, extensions)
            is SourceType.Subreddit -> queryUserMedia(sourceType.name, limit, extensions)
            else -> emptyList()
        }

        return RedditMetadata(url, media.toTypedArray(), source = sourceType)
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
        val result = api.getUserSubmissions(name, "", 100)
        return emptyList()
    }
    // endregion

    companion object {
        fun isUrlMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("reddit.com", true)
        }
    }
}