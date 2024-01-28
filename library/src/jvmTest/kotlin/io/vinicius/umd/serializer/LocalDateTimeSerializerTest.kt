package io.vinicius.umd.serializer

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class LocalDateTimeDto(
    @Serializable(LocalDateTimeSerializer::class)
    val date: LocalDateTime,
)

class LocalDateTimeSerializerTest {
    @Test
    fun `Epoch time is deserialized to LocalDateTime`() {
        val dto = Json.decodeFromString(LocalDateTimeDto.serializer(), """{"date":1705168660}""")
        assertEquals(LocalDate(2024, 1, 13), dto.date.date)
    }

    @Test
    fun `LocalDateTime is serialized to Epoch`() {
        val dto = LocalDateTimeDto(LocalDateTime(2024, 1, 13, 17, 57, 40))
        val json = Json.encodeToString(LocalDateTimeDto.serializer(), dto)
        assertEquals("""{"date":1705168660}""", json)
    }
}