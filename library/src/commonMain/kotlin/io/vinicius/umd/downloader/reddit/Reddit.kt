package io.vinicius.umd.downloader.reddit

import io.ktor.http.Url
import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.model.DownloaderType
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.EventCallback
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Response

internal class Reddit(
    private val event: EventCallback?,
    private val api: Contract = RedditApi(),
) : Downloader {
    init {
        event?.invoke(Event.OnDownloaderFound("reddit"))
    }

    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response {
        var sourceName = ""
        val source = getSourceType(url)

        val submissions = when (source) {
            is SourceType.User -> {
                sourceName = source.name
                fetchSubmissions(source, sourceName, limit, extensions)
            }

            is SourceType.Subreddit -> {
                sourceName = source.name
                fetchSubmissions(source, sourceName, limit, extensions)
            }

            else -> emptyList()
        }

        val media = submissionsToMedia(submissions, source, sourceName)
        return Response(url, media, DownloaderType.Reddit)
    }

    fun getSourceType(url: String): SourceType {
        val regexUser = """/(?:user|u)/([^/?]+)""".toRegex()
        val regexSubreddit = """/r/([^/?]+)""".toRegex()

        val source = when {
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

        event?.invoke(Event.OnDownloadTypeFound(source::class.simpleName?.lowercase().orEmpty()))
        return source
    }

    // region - Private methods
    private suspend fun fetchSubmissions(
        source: SourceType,
        name: String,
        limit: Int,
        extensions: List<String>,
    ): List<Child> {
        val submissions = mutableSetOf<Child>()
        var after: String? = ""

        do {
            val response = if (source is SourceType.User) {
                api.getUserSubmissions(name, after.orEmpty(), 100)
            } else {
                api.getSubredditSubmissions(name, after.orEmpty(), 100)
            }

            val filteredSubmissions = if (extensions.isNotEmpty()) {
                response.data.children.filter { extensions.contains(it.data.extension) }
            } else {
                response.data.children
            }

            after = response.data.after
            val amountBefore = submissions.size
            submissions.addAll(filteredSubmissions)
            val amountAfter = submissions.size

            event?.invoke(Event.OnMediaQueried(amountAfter - amountBefore))
        } while (response.data.children.isNotEmpty() && submissions.size < limit && after != null)

        // Sorting by creation date
        event?.invoke(Event.OnQueryCompleted(submissions.size))
        return submissions.sortedBy { it.data.created }
    }

    private fun submissionsToMedia(submissions: List<Child>, source: SourceType, name: String): List<Media> {
        return submissions
            .map {
                Media(
                    it.data.url.toString(),
                    mapOf(
                        "sourceType" to source::class.simpleName?.lowercase(),
                        "sourceName" to name,
                        "created" to it.data.created.toString(),
                    ),
                )
            }
    }
    // endregion

    companion object {
        fun isMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("reddit.com", true)
        }
    }
}