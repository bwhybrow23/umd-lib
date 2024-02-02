package io.vinicius.umd.util

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.minutes

class FetchTest {
    private val fetch = Fetch()

    @Test
    fun `HTTP GET result is 200 OK`() = runTest(timeout = 1.minutes) {
        val response = fetch.getString("https://httpstat.us/200")
        assertEquals("200 OK", response)
    }

    @Test
    fun `HTTP GET result is 429 Too Many Requests`() = runTest(timeout = 1.minutes) {
        assertFailsWith<FetchException> { fetch.getString("https://httpstat.us/429") }
    }
}