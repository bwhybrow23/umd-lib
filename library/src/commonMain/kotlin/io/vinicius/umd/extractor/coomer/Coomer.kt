package io.vinicius.umd.extractor.coomer

import co.touchlab.kermit.Logger
import com.fleeksoft.ksoup.Ksoup
import io.ktor.http.Url
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.ktx.cleanUrl
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.ExtractorType
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Response
import io.vinicius.umd.util.Fetch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.math.max

internal class Coomer(
    private val fetch: Fetch = Fetch(),
) : Extractor {
    override val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)

    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response {
        val source = getSourceType(url)

        val media = when (source) {
            is SourceType.User -> fetchUser(source, limit, extensions)
            is SourceType.Post -> fetchPost(source, limit, extensions)
        }

        events.tryEmit(Event.OnQueryCompleted(media.size))
        Logger.d("Coomer") { "Query completed: ${media.size} media found" }

        return Response(url, media, ExtractorType.Coomer)
    }

    // region - Private methods
    private fun getSourceType(url: String): SourceType {
        val regexPost = """(onlyfans|fansly)/user/([^/]+)/post/([^/\n?]+)""".toRegex()
        val regexUser = """(onlyfans|fansly)/user/([^/\n?]+)""".toRegex()
        val user: String

        val source = when {
            url.contains(regexPost) -> {
                val groups = regexPost.find(url)?.groupValues
                user = groups?.get(2).orEmpty()
                SourceType.Post(groups?.get(1).orEmpty(), user, groups?.get(3).orEmpty())
            }

            url.contains(regexUser) -> {
                val groups = regexUser.find(url)?.groupValues
                user = groups?.get(2).orEmpty()
                SourceType.User(groups?.get(1).orEmpty(), user)
            }

            else -> throw IllegalArgumentException("No support for Coomer URL: $url")
        }

        val sourceName = source::class.simpleName?.lowercase().orEmpty()
        events.tryEmit(Event.OnExtractorTypeFound(sourceName, user))
        Logger.d("Coomer") { "Extractor type found: $sourceName" }

        return source
    }

    private suspend fun fetchUser(source: SourceType.User, limit: Int, extensions: List<String>): List<Media> {
        val media = mutableSetOf<Media>()
        val numPages = countPages("https://coomer.su/${source.service}/user/${source.user}")

        for (i in 0..<numPages) {
            val url = "https://coomer.su/${source.service}/user/${source.user}?o=${i * 50}"
            val postUrls = getPostUrls(url)

            for (postUrl in postUrls) {
                val postMedia = getPostMedia(postUrl, source.service, source.user)
                val filteredMedia = postMedia.filter { extensions.isEmpty() || extensions.contains(it.extension) }

                val amountBefore = media.size
                media.addAll(filteredMedia)

                val queried = media.size - amountBefore
                events.tryEmit(Event.OnMediaQueried(queried))
                Logger.d("Coomer") { "Media queried: $queried" }

                if (media.size >= limit) break
            }

            if (media.size >= limit) break
        }

        val filteredMedia = media.take(limit)
        return filteredMedia
    }

    private suspend fun fetchPost(source: SourceType.Post, limit: Int, extensions: List<String>): List<Media> {
        val url = "https://coomer.su/${source.service}/user/${source.user}/post/${source.id}"
        val media = getPostMedia(url, source.service, source.user)
        val filteredMedia = media
            .filter { extensions.isEmpty() || extensions.contains(it.extension) }
            .take(limit)

        Logger.d("Coomer") { "Media queried: ${filteredMedia.size}" }
        return filteredMedia
    }

    private suspend fun countPages(url: String): Int {
        val html = fetch.getString(url)
        val doc = Ksoup.parse(html)
        val result = doc.select("div#paginator-top small")
        val regex = """of (\d+)""".toRegex()
        val groups = regex.find(result.text())?.groupValues
        val num = groups?.get(1)?.toFloat() ?: 0f
        val pages = ceil(num / 50).toInt()

        val numPages = max(pages, 1)
        Logger.d("Coomer") { "Number of pages found: $numPages" }

        return max(pages, 1)
    }

    private suspend fun getPostUrls(url: String): List<String> {
        val html = fetch.getString(url)
        val doc = Ksoup.parse(html)
        val results = doc.select("article")

        return results.map {
            val id = it.attr("data-id")
            val service = it.attr("data-service")
            val user = it.attr("data-user")

            "https://coomer.su/$service/user/$user/post/$id"
        }
    }

    private suspend fun getPostMedia(url: String, service: String, user: String): List<Media> {
        val html = fetch.getString(url)
        val doc = Ksoup.parse(html)
        val postId = doc.select("meta[name='id']").attr("content")
        val result = doc.select("div.post__published")
        val regex = """Published: (.+)""".toRegex()
        val groups = regex.find(result.text())?.groupValues
        val dateTime = groups?.get(1)?.replace(" ", "T")?.toLocalDateTime().toString()

        val images = doc.select("a.fileThumb").map {
            Media(
                Url(it.attr("href")).cleanUrl(),
                mapOf(
                    "source" to service,
                    "name" to user,
                    "id" to postId,
                    "created" to dateTime,
                ),
            )
        }

        val videos = doc.select("a.post__attachment-link").map {
            Media(
                Url(it.attr("href")).cleanUrl(),
                mapOf(
                    "source" to service,
                    "name" to user,
                    "id" to postId,
                    "created" to dateTime,
                ),
            )
        }

        return images + videos
    }
    // endregion

    companion object {
        fun isMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("coomer.su", true)
        }
    }
}