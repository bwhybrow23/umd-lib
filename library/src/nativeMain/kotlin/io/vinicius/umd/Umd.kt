package io.vinicius.umd

import io.vinicius.umd.model.Event
import io.vinicius.umd.model.Response
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Umd {
    lateinit var events: SharedFlow<Event>
        private set

    suspend fun queryMedia(url: String, limit: Int = Int.MAX_VALUE, extensions: List<String> = emptyList()): Response {
        val lowercaseExt = extensions.map { it.lowercase() }
        val extractor = findExtractor(url)
        events = extractor.events.asSharedFlow()
        extractor.events.tryEmit(Event.OnExtractorFound(extractor::class.simpleName?.lowercase().orEmpty()))

        return extractor.queryMedia(url, limit, lowercaseExt)
    }
}