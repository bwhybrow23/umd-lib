package io.vinicius.umd.util

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.minutes

class FetchTest {
    private val fetch = Fetch()

    @Test
    fun `HTTP GET result is 200 OK`() = runTest(timeout = 1.minutes) {
        fetch.getFlow("https://httpstat.us/200").test {
            assertEquals("200 OK", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `HTTP GET result is 429 Too Many Requests`() = runTest(timeout = 1.minutes) {
        fetch.getFlow("https://httpstat.us/429").test {
            val exception = awaitError()
            assertIs<FetchException>(exception)
            assertEquals(429, exception.statusCode)
            cancelAndIgnoreRemainingEvents()
        }
    }
}