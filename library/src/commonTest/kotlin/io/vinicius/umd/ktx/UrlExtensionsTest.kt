package io.vinicius.umd.ktx

import io.ktor.http.Url
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlExtensionsTest {
    @Test
    @JsName("queryParametersAreRemovedFromTheUrl")
    fun `Query parameters are removed from the URL`() {
        val url = Url("https://www.example.com/path?query=param#fragment")
        assertEquals("https://www.example.com/path", url.cleanUrl())
    }
}