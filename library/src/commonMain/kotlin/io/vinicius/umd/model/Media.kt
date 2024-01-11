package io.vinicius.umd.model

enum class MediaType {
    Image,
    Video
}

data class Media(
    val url: String,
    val filePath: String,
    val mediaType: MediaType
)