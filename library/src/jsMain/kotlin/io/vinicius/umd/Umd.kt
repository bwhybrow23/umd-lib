package io.vinicius.umd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

internal val scope = CoroutineScope(Dispatchers.Default)

@JsExport
fun queryMedia(url: String, limit: Int = Int.MAX_VALUE, extensions: Array<String> = emptyArray()) = scope.promise {
    val lowercaseExt = extensions.map { it.lowercase() }
    val extractor = findExtractor(url)
    extractor.queryMedia(url, limit, lowercaseExt)
}