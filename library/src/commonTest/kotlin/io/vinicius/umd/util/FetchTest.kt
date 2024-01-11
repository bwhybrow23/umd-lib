package io.vinicius.umd.util

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class FetchTest {
    private val fetch = Fetch()

    @Test
    fun `The HTTP GET result is not empty`() {
        val result = runBlocking { fetch.getString("https://httpbin.org/get") }
        assertTrue(result.isNotEmpty())
        assertTrue(result.contains("httpbin.org"))
    }
}