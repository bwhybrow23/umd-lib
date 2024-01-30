package io.vinicius.umd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class MainTest {
    private val umd = Umd()

    @Test
    fun `User submissions are fetched`() = runTest(timeout = 1.hours) {
        val response = umd.queryMedia("https://www.reddit.com/user/SerlianaElle/")
        assertTrue(response.media.isNotEmpty())
    }

    @Test
    fun `Subreddit submissions are fetched`() = runTest(timeout = 1.hours) {
        val response = umd.queryMedia("https://www.reddit.com/r/bimbofetish/")
        assertTrue(response.media.isNotEmpty())
    }
}