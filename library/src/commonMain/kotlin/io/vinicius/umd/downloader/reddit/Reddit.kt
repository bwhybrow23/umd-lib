package io.vinicius.umd.downloader.reddit

import io.ktor.http.Url
import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.downloader.reddit.model.RedditMetadata
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Metadata
import kotlinx.coroutines.runBlocking

class Reddit : Downloader {
    private val api = RedditApi()

    override fun isUrlMatch(url: String): Boolean {
        val urlObj = Url(url)
        return urlObj.host.endsWith("reddit.com", true)
    }

    override fun fetchMedia(url: String, limit: Int, extensions: List<String>): Metadata {
        val sourceType = Util.getSourceType(url)

        val media = when(sourceType) {
            is SourceType.User -> queryUserMedia(sourceType.name, limit, extensions)
            is SourceType.Subreddit -> queryUserMedia(sourceType.name, limit, extensions)
            else -> emptyList()
        }

        return RedditMetadata(url, media, source = sourceType)
    }

    // region - Private methods
    private fun queryUserMedia(name: String, limit: Int, extensions: List<String>): List<Media> {
        val result = runBlocking { api.getUserSubmissions(name, "", 100) }
        return emptyList()
    }
    // endregion
}