package io.vinicius.umd

import io.vinicius.umd.exception.InvalidExtractorException
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.extractor.coomer.Coomer
import io.vinicius.umd.extractor.reddit.Reddit
import io.vinicius.umd.extractor.redgifs.Redgifs
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.EventCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

internal val scope = CoroutineScope(Dispatchers.Default)

@JsExport
class Umd(private val url: String, val callback: EventCallback? = null) {
    private val extractor = findExtractor(url)

    fun queryMedia(limit: Int = Int.MAX_VALUE, extensions: Array<String> = emptyArray()) = scope.promise {
        callback?.invoke(Event.OnExtractorFound(extractor::class.simpleName?.lowercase().orEmpty()))
        val lowercaseExt = extensions.map { it.lowercase() }
        extractor.queryMedia(url, limit, lowercaseExt)
    }

    // region - Private methods
    private fun findExtractor(url: String): Extractor {
        return when {
            Coomer.isMatch(url) -> Coomer(callback = callback)
            Reddit.isMatch(url) -> Reddit(callback = callback)
            Redgifs.isMatch(url) -> Redgifs(callback = callback)
            else -> throw InvalidExtractorException(url, "No extractor found for URL: $url")
        }
    }
    // endregion
}