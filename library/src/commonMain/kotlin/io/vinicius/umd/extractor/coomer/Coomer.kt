package io.vinicius.umd.extractor.coomer

import com.fleeksoft.ksoup.Ksoup
import io.ktor.http.Url
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.ExtractorType
import io.vinicius.umd.model.Response
import io.vinicius.umd.util.Fetch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.math.ceil
import kotlin.math.max

internal class Coomer : Extractor {
    override val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    private val fetch = Fetch()

    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response {
        val source = getSourceType(url)

        when (source) {
            is SourceType.User -> {
                fetchPosts(source.service, source.user)
            }

            is SourceType.Post -> {}
        }

        return Response(url, emptyList(), ExtractorType.Reddit)
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

        events.tryEmit(Event.OnExtractorTypeFound(source::class.simpleName?.lowercase().orEmpty(), user))
        return source
    }

    private suspend fun fetchPosts(service: String, user: String) {
        val numPages = countPages("https://coomer.su/${service}/user/${user}")
        println(numPages)
    }

    private suspend fun countPages(url: String): Int {
        val html = fetch.getString(url)
        val doc = Ksoup.parse(html)
        val result = doc.select("div#paginator-top small")
        val regex = """of (\d+)""".toRegex()
        val groups = regex.find(result.text())?.groupValues
        val num = groups?.get(1)?.toFloat() ?: 0f
        val pages = ceil(num / 50).toInt()

        return max(pages, 1)
    }
    // endregion

    companion object {
        fun isMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("coomer.su", true)
        }
    }
}