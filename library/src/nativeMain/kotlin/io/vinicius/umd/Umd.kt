package io.vinicius.umd

import co.touchlab.kermit.Logger
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.extractor.coomer.Coomer
import io.vinicius.umd.extractor.reddit.Reddit
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.EventCallback
import io.vinicius.umd.model.Response

class Umd(private val url: String, val callback: EventCallback? = null) {
    private val extractor = findExtractor(url)

    @DefaultArgumentInterop.Enabled
    suspend fun queryMedia(limit: Int = Int.MAX_VALUE, extensions: List<String> = emptyList()): Response {
        // Sending event
        val extractorName = extractor::class.simpleName?.lowercase().orEmpty()
        callback?.invoke(Event.OnExtractorFound(extractorName))
        Logger.d("Umd") { "Extractor found: $extractorName" }

        val lowercaseExt = extensions.map { it.lowercase() }
        return extractor.queryMedia(url, limit, lowercaseExt)
    }

    // region - Private methods
    private fun findExtractor(url: String): Extractor {
        return when {
            Coomer.isMatch(url) -> Coomer(callback = callback)
            Reddit.isMatch(url) -> Reddit(callback = callback)
            else -> throw IllegalArgumentException("No extractor found for URL: $url")
        }
    }
    // endregion
}