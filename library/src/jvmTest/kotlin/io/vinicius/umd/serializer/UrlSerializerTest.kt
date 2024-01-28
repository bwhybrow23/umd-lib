package io.vinicius.umd.serializer

import io.ktor.http.Url
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
private data class UrlDto(
    @Serializable(UrlSerializer::class)
    val url: Url,
)

class UrlSerializerTest {
    @Test
    fun `String is deserialized to Url`() {
        val expected = Url("https://www.example.com/path?query=param#fragment")
        val json = """{"url":"https://www.example.com/path?query=param#fragment"}"""
        val dto = Json.decodeFromString(UrlDto.serializer(), json)
        assertEquals(expected, dto.url)
    }

    @Test
    fun `Url is serialized to String`() {
        val dto = UrlDto(Url("https://www.example.com/path?query=param#fragment"))
        val json = Json.encodeToString(UrlDto.serializer(), dto)
        assertEquals("""{"url":"https://www.example.com/path?query=param#fragment"}""", json)
    }
}