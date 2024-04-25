package io.vinicius.umd.ktx

import com.eygraber.uri.Url
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlExtensionsTest {
    @Test
    fun `Query parameters are removed from the URL`() {
        val url = Url.parse("https://www.example.com/path?query=param#fragment")
        assertEquals("https://www.example.com/path", url.cleanUrl)
    }
}