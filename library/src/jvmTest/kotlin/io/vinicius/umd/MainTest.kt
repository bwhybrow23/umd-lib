package io.vinicius.umd

import io.vinicius.umd.model.Event
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class MainTest {
    @Test
    fun `User submissions are fetched`() = runTest(timeout = 1.hours) {
        val response = queryMedia("https://www.reddit.com/user/SerlianaElle/")
        assertTrue(response.media.isNotEmpty())
    }

    @Test
    fun `Subreddit submissions are fetched`() = runTest(timeout = 1.hours) {
        val response = queryMedia("https://www.reddit.com/r/bimbofetish/") {
            if (it is Event.OnMediaQueried) {
                println(it.amount)
            }
        }

        assertTrue(response.media.isNotEmpty())
    }
}