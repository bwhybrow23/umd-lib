package io.vinicius.umd.model

data class Media(
    val url: String,
    val metadata: Map<String, Any?> = emptyMap(),
) {
    val extension = url.substringAfterLast(".", "")
        .lowercase()
        .ifEmpty { null }

    val mediaType = when (extension) {
        "jpg", "jpeg", "png", "gif", "avif" -> MediaType.Image
        ".gifv", "mp4", "m4v", "webm", "mkv" -> MediaType.Video
        else -> MediaType.Unknown
    }
}