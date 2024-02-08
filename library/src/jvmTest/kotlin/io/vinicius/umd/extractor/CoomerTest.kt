package io.vinicius.umd.extractor

import app.cash.turbine.test
import io.vinicius.umd.Umd
import io.vinicius.umd.model.Event
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.minutes

class CoomerTest {
    @Test
    fun `Coomer extractor identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://coomer.su/onlyfans/user/atomicbrunette18",
            "https://www.coomer.su/onlyfans/user/atomicbrunette18",
        ).forEach {
            val umd = Umd(it)

            umd.events.test {
                umd.queryMedia(0)
                val event = awaitItem()
                assertIs<Event.OnExtractorFound>(event)
                assertEquals("coomer", event.name)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Coomer extractor identified, but URL is not supported`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://coomer.su/artists",
            "https://coomer.su/account/register",
        ).forEach {
            val umd = Umd(it)
            assertFails { umd.queryMedia(0) }
        }
    }

    @Test
    fun `Reddit extractor NOT identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://example.com/coomer.su",
            "https://www.google.com",
        ).forEach {
            assertFails { Umd(it) }
        }
    }

    @Test
    fun `Extractor type is 'user'`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://coomer.su/onlyfans/user/atomicbrunette18",
            "https://coomer.su/onlyfans/user/atomicbrunette18/",
            "https://coomer.su/onlyfans/user/atomicbrunette18?o=50",
        ).forEach {
            val umd = Umd(it)

            umd.events.test {
                umd.queryMedia(0)
                skipItems(1) // Skip OnExtractorFound
                val event = awaitItem()
                assertIs<Event.OnExtractorTypeFound>(event)
                assertEquals("user", event.type)
                assertEquals("atomicbrunette18", event.name)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}