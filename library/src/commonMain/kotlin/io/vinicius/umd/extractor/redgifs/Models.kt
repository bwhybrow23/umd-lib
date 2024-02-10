package io.vinicius.umd.extractor.redgifs

import io.vinicius.umd.serializer.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Auth(
    val token: String,
    val session: String,
)

@Serializable
internal data class Watch(
    val gif: Video,
) {
    @Serializable
    internal data class Video(
        val id: String,
        val userName: String,
        val duration: Double,

        @SerialName("urls")
        val url: Url,

        @SerialName("createDate")
        @Serializable(LocalDateTimeSerializer::class)
        val created: LocalDateTime,
    ) {
        @Serializable
        data class Url(
            val poster: String,
            val thumbnail: String,
            val vthumbnail: String,
            val hd: String,
            val sd: String,
        )
    }
}