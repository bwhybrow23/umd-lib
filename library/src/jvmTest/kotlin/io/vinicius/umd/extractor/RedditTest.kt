package io.vinicius.umd.extractor

import app.cash.turbine.test
import io.vinicius.umd.extractor.reddit.Reddit
import io.vinicius.umd.model.Event
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

class RedditTest {
    @Test
    fun `Reddit URLs are identified by the downloader`() {
        var url = "https://reddit.com/user/SerlianaElle/comments/192lqo2/upvote_this_and_say_yes_if_i_made_you_stop/"
        assertTrue(Reddit.isMatch(url), "Reddit URL without www. matches")

        url = "https://www.reddit.com/user/SerlianaElle/comments/192lqo2/upvote_this_and_say_yes_if_i_made_you_stop/"
        assertTrue(Reddit.isMatch(url), "Reddit URL with www. matches")

        url = "https://www.google.com"
        assertFalse(Reddit.isMatch(url), "Google doesn't match Reddit URL")
    }

    @Test
    fun `Extractor type is 'user'`() = runTest(timeout = 1.minutes) {
        val extractor = Reddit()

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/user/mir_bby", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertEquals("user", event.type)
            assertEquals("mir_bby", event.name)
            cancelAndIgnoreRemainingEvents()
        }

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/user/mir_bby/", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertEquals("user", event.type)
            assertEquals("mir_bby", event.name)
            cancelAndIgnoreRemainingEvents()
        }

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/user/mir_bby/submitted/", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertEquals("user", event.type)
            assertEquals("mir_bby", event.name)
            cancelAndIgnoreRemainingEvents()
        }

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/u/mir_bby", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertEquals("user", event.type)
            assertEquals("mir_bby", event.name)
            cancelAndIgnoreRemainingEvents()
        }

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/u/mir_bby/", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertEquals("user", event.type)
            assertEquals("mir_bby", event.name)
            cancelAndIgnoreRemainingEvents()
        }

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/u/mir_bby/submitted/", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertEquals("user", event.type)
            assertEquals("mir_bby", event.name)
            cancelAndIgnoreRemainingEvents()
        }

        extractor.events.test {
            extractor.queryMedia("https://www.reddit.com/r/pics/", 0, emptyList())
            val event = awaitItem()
            assertIs<Event.OnExtractorTypeFound>(event)
            assertNotEquals("user", event.type)
            cancelAndIgnoreRemainingEvents()
        }
    }
}