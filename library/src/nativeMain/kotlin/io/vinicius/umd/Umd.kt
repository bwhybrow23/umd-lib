package io.vinicius.umd

import io.vinicius.umd.model.EventCallback
import io.vinicius.umd.model.Response

suspend fun queryMedia(
    url: String,
    limit: Int = Int.MAX_VALUE,
    extensions: List<String> = emptyList(),
    event: EventCallback? = null,
): Response {
    val lowercaseExt = extensions.map { it.lowercase() }
    val extractor = findExtractor(url, event)
    return extractor.queryMedia(url, limit, lowercaseExt)
}