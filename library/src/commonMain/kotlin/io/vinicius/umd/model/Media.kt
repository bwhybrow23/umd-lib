package io.vinicius.umd.model

import com.eygraber.uri.Url
import io.vinicius.umd.ktx.extension

data class Media(
    val url: String,
    val metadata: Map<String, Any?> = emptyMap(),
) {
    val extension = Url.parse(url).extension

    val mediaType = when (extension) {
        "jpg", "jpeg", "png", "gif", "avif" -> MediaType.Image
        "gifv", "mp4", "m4v", "webm", "mkv" -> MediaType.Video
        else -> MediaType.Unknown
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Media
        return url == other.url
    }

    override fun hashCode(): Int = url.hashCode()
    override fun toString() = "Media(url='$url', extension=$extension, mediaType=$mediaType, metadata=$metadata)"
}