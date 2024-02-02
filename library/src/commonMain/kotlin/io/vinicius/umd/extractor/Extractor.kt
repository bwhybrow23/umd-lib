package io.vinicius.umd.extractor

import io.vinicius.umd.model.Event
import io.vinicius.umd.model.Response
import kotlinx.coroutines.flow.MutableSharedFlow

internal interface Extractor {
    val events: MutableSharedFlow<Event>
    suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response
}