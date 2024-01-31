package io.vinicius.umd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class MainTest {
    @Test
    fun `User submissions are fetched`() = runTest(timeout = 1.hours) {
        val umd = Umd("https://www.reddit.com/user/SerlianaElle/")
        val response = umd.queryMedia()
        assertTrue(response.media.isNotEmpty())
    }

    @Test
    fun `Subreddit submissions are fetched`() = runTest(timeout = 1.hours) {
        val umd = Umd("https://www.reddit.com/user/SerlianaElle/")
        val response = umd.queryMedia()
        assertTrue(response.media.isNotEmpty())
    }
}