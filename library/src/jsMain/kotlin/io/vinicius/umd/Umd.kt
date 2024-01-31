package io.vinicius.umd

import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.extractor.coomer.Coomer
import io.vinicius.umd.extractor.reddit.Reddit
import io.vinicius.umd.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.promise

internal val scope = CoroutineScope(Dispatchers.Default)

@JsExport
class Umd(private val url: String) {
    private val extractor = findExtractor(url)
    val events = extractor.events.asSharedFlow()

    fun queryMedia(limit: Int = Int.MAX_VALUE, extensions: Array<String> = emptyArray()) = scope.promise {
        extractor.events.tryEmit(Event.OnExtractorFound(extractor::class.simpleName?.lowercase().orEmpty()))
        val lowercaseExt = extensions.map { it.lowercase() }
        extractor.queryMedia(url, limit, lowercaseExt)
    }

    // region - Private methods
    private fun findExtractor(url: String): Extractor {
        return when {
            Coomer.isMatch(url) -> Coomer()
            Reddit.isMatch(url) -> Reddit()
            else -> throw IllegalArgumentException("No extractor found for URL: $url")
        }
    }
    // endregion
}