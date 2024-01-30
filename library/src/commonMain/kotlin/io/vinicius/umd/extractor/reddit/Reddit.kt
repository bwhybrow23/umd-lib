package io.vinicius.umd.extractor.reddit

import io.ktor.http.Url
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.ExtractorType
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Response
import kotlinx.coroutines.flow.MutableSharedFlow

internal class Reddit(
    private val api: Contract = RedditApi(),
) : Extractor {
    override val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)

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
        return Response(url, media, ExtractorType.Reddit)
    }

    // region - Private methods
    private fun getSourceType(url: String): SourceType {
        val regexUser = """/(?:u|user)/([^/\n?]+)""".toRegex()
        val regexSubreddit = """/r/([^/\n]+)""".toRegex()
        var name: String? = null

        val source = when {
            url.contains(regexUser) -> {
                val groups = regexUser.find(url)?.groupValues
                name = groups?.get(1).orEmpty()
                SourceType.User(name)
            }

            url.contains(regexSubreddit) -> {
                val groups = regexSubreddit.find(url)?.groupValues
                name = groups?.get(1).orEmpty()
                SourceType.Subreddit(name)
            }

            else -> SourceType.Unknown
        }

        events.tryEmit(Event.OnExtractorTypeFound(source::class.simpleName?.lowercase().orEmpty(), name))
        return source
    }

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

            events.tryEmit(Event.OnMediaQueried(amountAfter - amountBefore))
        } while (response.data.children.isNotEmpty() && submissions.size < limit && after != null)

        // Sorting by creation date
        events.tryEmit(Event.OnQueryCompleted(submissions.size))
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