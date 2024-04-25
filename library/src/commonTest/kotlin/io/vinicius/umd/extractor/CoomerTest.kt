package io.vinicius.umd.extractor

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import io.vinicius.umd.Umd
import io.vinicius.umd.model.Event
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.time.Duration.Companion.minutes

class CoomerTest {
    init {
        Logger.setMinSeverity(Severity.Error)
    }

    @Test
    fun `Coomer extractor identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://coomer.su/onlyfans/user/atomicbrunette18",
            "https://www.coomer.su/onlyfans/user/atomicbrunette18",
        ).forEach {
            var numEvents = 0
            val umd = Umd(it) { event ->
                if (event is Event.OnExtractorFound) {
                    assertEquals("coomer", event.name)
                    numEvents++
                }
            }

            umd.queryMedia(0)
            assertEquals(1, numEvents)
        }
    }

    @Test
    fun `Coomer extractor identified but URL is not supported`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://coomer.su/artists",
            "https://coomer.su/account/register",
        ).forEach {
            val umd = Umd(it)
            assertFails { umd.queryMedia(0) }
        }
    }

    @Test
    fun `Coomer extractor NOT identified`() {
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
            var numEvents = 0
            val umd = Umd(it) { event ->
                if (event is Event.OnExtractorTypeFound) {
                    assertEquals("user", event.type)
                    assertEquals("atomicbrunette18", event.name)
                    numEvents++
                }
            }

            umd.queryMedia(0)
            assertEquals(1, numEvents)
        }
    }
}