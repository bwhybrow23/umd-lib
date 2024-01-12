package io.vinicius.umd.util

import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertTrue

class FetchTest {
    private val fetch = Fetch()

    @Test
    @JsName("d")
    fun `The HTTP GET result is not empty`() = runTest {
        val result = fetch.getString("https://httpbin.org/get")
        assertTrue(result.isNotEmpty())
        assertTrue(result.contains("httpbin.org"))
    }
}