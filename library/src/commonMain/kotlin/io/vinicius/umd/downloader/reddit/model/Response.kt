package io.vinicius.umd.downloader.reddit.model

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val data: ResponseData
) {
    @Serializable
    data class ResponseData(
        val after: String,
        val children: List<Child>
    )
}

@Serializable
data class Child(
    val data: ChildData
) {
    @Serializable
    data class ChildData(
        val author: String,
        val domain: String,
        val url: String,
//        val postHint: String,
//        val created: Long,
//        val isGallery: Boolean
    )
}