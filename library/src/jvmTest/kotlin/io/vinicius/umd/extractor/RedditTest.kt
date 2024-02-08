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

class RedditTest {
    @Test
    fun `Reddit extractor identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://reddit.com/user/SerlianaElle/comments/192lqo2/upvote_this_and_say_yes_if_i_made_you_stop/",
            "https://www.reddit.com/user/SerlianaElle/comments/192lqo2/upvote_this_and_say_yes_if_i_made_you_stop/",
        ).forEach {
            val umd = Umd(it)

            umd.events.test {
                umd.queryMedia(0)
                val event = awaitItem()
                assertIs<Event.OnExtractorFound>(event)
                assertEquals("reddit", event.name)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Reddit extractor identified, but URL is not supported`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://www.reddit.com/premium",
            "https://www.reddit.com/settings/",
        ).forEach {
            val umd = Umd(it)
            assertFails { umd.queryMedia(0) }
        }
    }

    @Test
    fun `Reddit extractor NOT identified`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://example.com/reddit.com",
            "https://www.google.com",
        ).forEach {
            assertFails { Umd(it) }
        }
    }

    @Test
    fun `Extractor type is 'user'`() = runTest(timeout = 1.minutes) {
        listOf(
            "https://www.reddit.com/u/mir_bby",
            "https://www.reddit.com/u/mir_bby/",
            "https://www.reddit.com/u/mir_bby/submitted/",
            "https://www.reddit.com/user/mir_bby",
            "https://www.reddit.com/user/mir_bby/",
            "https://www.reddit.com/user/mir_bby/submitted/",
        ).forEach {
            val umd = Umd(it)

            umd.events.test {
                umd.queryMedia(0)
                skipItems(1) // Skip OnExtractorFound
                val event = awaitItem()
                assertIs<Event.OnExtractorTypeFound>(event)
                assertEquals("user", event.type)
                assertEquals("mir_bby", event.name)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}