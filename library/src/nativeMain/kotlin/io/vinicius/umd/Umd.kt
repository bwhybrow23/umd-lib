package io.vinicius.umd

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.extractor.coomer.Coomer
import io.vinicius.umd.extractor.reddit.Reddit
import io.vinicius.umd.model.Event
import io.vinicius.umd.model.Response
import kotlinx.coroutines.flow.asSharedFlow

class Umd(private val url: String) {
    private val extractor = findExtractor(url)
    val events = extractor.events.asSharedFlow()
    var debugMode: Boolean = false
        set(value) = Logger.setMinSeverity(if (value) Severity.Debug else Severity.Info)

    init {
        debugMode = false
    }

    @DefaultArgumentInterop.Enabled
    suspend fun queryMedia(limit: Int = Int.MAX_VALUE, extensions: List<String> = emptyList()): Response {
        // Sending event
        extractor.events.tryEmit(Event.OnExtractorFound(extractor::class.simpleName?.lowercase().orEmpty()))

        val lowercaseExt = extensions.map { it.lowercase() }
        return extractor.queryMedia(url, limit, lowercaseExt)
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