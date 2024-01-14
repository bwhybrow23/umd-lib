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
    val downloader = findDownloader(url, event)
    return downloader.queryMedia(url, limit, lowercaseExt)
}