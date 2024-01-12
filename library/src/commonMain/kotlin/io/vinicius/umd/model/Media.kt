package io.vinicius.umd.model

import kotlin.js.JsExport

@JsExport
enum class MediaType {
    Image,
    Video
}

@JsExport
data class Media(
    val url: String,
    val filePath: String,
    val mediaType: MediaType
)