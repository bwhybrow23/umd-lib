package io.vinicius.umd.extractor.reddit

import co.touchlab.kermit.Logger
import io.ktor.http.Url
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.EventCallback
import io.vinicius.umd.model.ExtractorType
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Response
import io.vinicius.umd.util.Fetch

internal class Reddit(
    private val api: Contract = RedditApi(),
    private val callback: EventCallback? = null,
) : Extractor {
    private val tag = "Reddit"

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
        }

        val media = submissionsToMedia(submissions, source, sourceName)
        callback?.invoke(Event.OnQueryCompleted(media.size))
        Logger.d(tag) { "Query completed: ${media.size} media found" }

        return Response(url, media, ExtractorType.Reddit, emptyMap())
    }

    override fun configureFetch(): Fetch = Fetch()

    // region - Private methods
    private fun getSourceType(url: String): SourceType {
        val regexUser = """/(?:u|user)/([^/\n?]+)""".toRegex()
        val regexSubreddit = """/r/([^/\n]+)""".toRegex()
        val name: String

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

            else -> throw IllegalArgumentException("No support for Reddit URL: $url")
        }

        val sourceName = source::class.simpleName?.lowercase().orEmpty()
        callback?.invoke(Event.OnExtractorTypeFound(sourceName, name))
        Logger.d(tag) { "Extractor type found: $sourceName" }

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

            val filteredSubmissions = response.data.children.filter {
                extensions.isEmpty() || extensions.contains(it.data.extension)
            }

            after = response.data.after
            val amountBefore = submissions.size
            submissions.addAll(filteredSubmissions)

            val queried = submissions.size - amountBefore
            callback?.invoke(Event.OnMediaQueried(queried))
            Logger.d(tag) { "Media queried: $queried" }
        } while (response.data.children.isNotEmpty() && submissions.size < limit && after != null)

        return submissions.take(limit)
    }

    private fun submissionsToMedia(submissions: List<Child>, source: SourceType, name: String): List<Media> {
        return submissions
            .map {
                Media(
                    it.data.url.toString(),
                    mapOf(
                        "source" to source::class.simpleName?.lowercase(),
                        "name" to name,
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