package io.vinicius.umd.extractor.coomer

import io.ktor.http.Url
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.ExtractorType
import io.vinicius.umd.model.Response
import kotlinx.coroutines.flow.MutableSharedFlow

internal class Coomer : Extractor {
    override val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)

    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response {
        val source = getSourceType(url)
        return Response(url, emptyList(), ExtractorType.Reddit)
    }

    private fun getSourceType(url: String): SourceType {
        val regexPost = """(onlyfans|fansly)/user/([^/]+)/post/([^/\n?]+)""".toRegex()
        val regexUser = """(onlyfans|fansly)/user/([^/\n?]+)""".toRegex()

        val source = when {
            url.contains(regexPost) -> {
                val groups = regexPost.find(url)?.groupValues
                SourceType.Post(groups?.get(1).orEmpty(), groups?.get(2).orEmpty(), groups?.get(3).orEmpty())
            }

            url.contains(regexUser) -> {
                val groups = regexUser.find(url)?.groupValues
                SourceType.User(groups?.get(1).orEmpty(), groups?.get(2).orEmpty())
            }

            else -> SourceType.Unknown
        }

        events.tryEmit(Event.OnExtractorTypeFound(source::class.simpleName?.lowercase().orEmpty()))
        return source
    }

    companion object {
        fun isMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("coomer.su", true)
        }
    }
}