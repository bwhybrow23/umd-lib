package io.vinicius.umd.model

import kotlin.js.JsExport

@JsExport
enum class MediaType {
    Image,
    Video,
    Unknown,
}

@JsExport
data class Media(val url: String) {
    val extension = url.substringAfterLast(".", "").ifEmpty { null }

    val mediaType = when (extension?.lowercase()) {
        "jpg", "jpeg", "png", "gif", "avif" -> MediaType.Image
        ".gifv", "mp4", "m4v", "webm", "mkv" -> MediaType.Video
        else -> MediaType.Unknown
    }
}