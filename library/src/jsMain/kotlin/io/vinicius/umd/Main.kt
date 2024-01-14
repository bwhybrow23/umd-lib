package io.vinicius.umd

import io.vinicius.umd.model.EventCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

internal val scope = CoroutineScope(Dispatchers.Default)

@JsExport
fun queryMedia(
    url: String,
    limit: Int = Int.MAX_VALUE,
    extensions: Array<String> = emptyArray(),
    event: EventCallback? = null,
) = scope.promise {
    val lowercaseExt = extensions.map { it.lowercase() }
    val downloader = findDownloader(url, event)
    downloader.queryMedia(url, limit, lowercaseExt)
}