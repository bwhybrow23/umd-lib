package io.vinicius.umd.downloader.reddit

import io.ktor.http.Url
import io.vinicius.umd.serializer.LocalDateTimeSerializer
import io.vinicius.umd.serializer.UrlSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Submission(
    val data: Data,
) {
    @Serializable
    data class Data(
        val after: String?,
        val children: List<Child>,
    )
}

@Serializable
internal data class Child(
    val data: Data,
) {
    @Serializable
    data class Data(
        val author: String,
        val domain: String,

        @Serializable(UrlSerializer::class)
        val url: Url,

        @SerialName("post_hint")
        val postHint: String = "unknown",

        @Serializable(LocalDateTimeSerializer::class)
        val created: LocalDateTime,

        @SerialName("is_gallery")
        val isGallery: Boolean = false,
    ) {
        override fun equals(other: Any?): Boolean {
            val data = other as? Data
            return url == data?.url
        }

        override fun hashCode(): Int {
            var result = author.hashCode()
            result = 31 * result + url.hashCode()
            return result
        }
    }
}