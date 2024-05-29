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

class kemonoTest {
    init {
        Logger.setMinSeverity(Severity.Error)
    }

    @Test
    fun `kemono extractor identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://kemono.su/patreon/user/5564244",
            "https://www.kemono.su/patreon/user/5564244",
        ).forEach {
            var numEvents = 0
            val umd = Umd(it) { event ->
                if (event is Event.OnExtractorFound) {
                    assertEquals("kemono", event.name)
                    numEvents++
                }
            }

            umd.queryMedia(0)
            assertEquals(1, numEvents)
        }
    }

    @Test
    fun `kemono extractor identified but URL is not supported`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://kemono.su/artists",
            "https://kemono.su/account/register",
        ).forEach {
            val umd = Umd(it)
            assertFails { umd.queryMedia(0) }
        }
    }

    @Test
    fun `kemono extractor NOT identified`() {
        listOf(
            "https://example.com/kemono.su",
            "https://www.google.com",
        ).forEach {
            assertFails { Umd(it) }
        }
    }

    @Test
    fun `Extractor type is 'user'`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://kemono.su/patreon/user/5564244",
            "https://kemono.su/patreon/user/5564244/",
            "https://kemono.su/patreon/user/5564244?o=50",
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