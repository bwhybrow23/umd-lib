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

class RedgifsTest {
    init {
        Logger.setMinSeverity(Severity.Error)
    }

    @Test
    fun `Redgifs extractor identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://www.redgifs.com/watch/woozymoralalbatross",
            "https://redgifs.com/watch/watchfullavendergodwit",
        ).forEach {
            var numEvents = 0
            val umd = Umd(it) { event ->
                if (event is Event.OnExtractorFound) {
                    assertEquals("redgifs", event.name)
                    numEvents++
                }
            }

            umd.queryMedia(0)
            assertEquals(1, numEvents)
        }
    }

    @Test
    fun `Redgifs extractor identified but URL is not supported`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://www.redgifs.com/explore",
            "https://www.redgifs.com/categories",
        ).forEach {
            val umd = Umd(it)
            assertFails { umd.queryMedia(0) }
        }
    }

    @Test
    fun `Redgifs extractor NOT identified`() {
        listOf(
            "https://example.com/reddit.com",
            "https://www.google.com",
        ).forEach {
            assertFails { Umd(it) }
        }
    }

    @Test
    fun `Extractor type is 'video'`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://www.redgifs.com/watch/unsungdenseflyingsquirrel",
            "https://www.redgifs.com/watch/grubbyfruitfultasmaniandevil",
            "https://www.redgifs.com/watch/assuredsneakyshoveler",
        ).forEach {
            var numEvents = 0
            val umd = Umd(it) { event ->
                if (event is Event.OnExtractorTypeFound) {
                    assertEquals("video", event.type)
                    numEvents++
                }
            }

            umd.queryMedia(0)
            assertEquals(1, numEvents)
        }
    }
}