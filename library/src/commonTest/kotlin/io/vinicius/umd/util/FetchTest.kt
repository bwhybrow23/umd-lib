package io.vinicius.umd.util

import io.vinicius.umd.exception.FetchException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

class FetchTest {
    private val fetch = Fetch()

    @Test
    fun `HTTP GET result is 200 OK`() = runTest(timeout = 1.minutes) {
        val response = fetch.getString("https://httpbin.org/get")
        assertTrue { response.contains("klopik") }
    }

    @Test
    fun `HTTP GET result is 429 Too Many Requests`() = runTest(timeout = 1.minutes) {
        assertFailsWith<FetchException> { fetch.getString("https://httpbin.org/status/429") }
    }

    @Test
    fun `Download file`() = runTest(timeout = 1.minutes) {
        fetch.downloadFile(
            "https://httpbin.org/image",
            "/Users/vegidio/Desktop/test.jpg",
        ) {
            println(it)
        }
    }
}