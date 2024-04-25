package io.vinicius.umd.serializer

import com.eygraber.uri.Url
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object UrlSerializer : KSerializer<Url> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Url", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Url) {
        val url = value.toString()
        encoder.encodeString(url)
    }

    override fun deserialize(decoder: Decoder): Url {
        val urlString = decoder.decodeString()
        return Url.parse(urlString)
    }
}