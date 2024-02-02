package io.vinicius.umd.serializer

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val epoch = value.toInstant(TimeZone.UTC).epochSeconds
        encoder.encodeLong(epoch)
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val epoch = decoder.decodeDouble().toLong()
        val instant = Instant.fromEpochSeconds(epoch)
        return instant.toLocalDateTime(TimeZone.UTC)
    }
}