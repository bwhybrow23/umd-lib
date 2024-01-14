package io.vinicius.umd.downloader

import io.vinicius.umd.downloader.reddit.Reddit
import io.vinicius.umd.downloader.reddit.SourceType
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class RedditDownloaderTest {
    private val downloader = Reddit()

    @Test
    @JsName("redditUrlsAreIdentifiedByTheDownloader")
    fun `Reddit URLs are identified by the downloader`() {
        var url = "https://reddit.com/user/SerlianaElle/comments/192lqo2/upvote_this_and_say_yes_if_i_made_you_stop/"
        assertTrue(Reddit.isMatch(url), "Reddit URL without www. matches")

        url = "https://www.reddit.com/user/SerlianaElle/comments/192lqo2/upvote_this_and_say_yes_if_i_made_you_stop/"
        assertTrue(Reddit.isMatch(url), "Reddit URL with www. matches")

        url = "https://www.google.com"
        assertFalse(Reddit.isMatch(url), "Google doesn't match Reddit URL")
    }

    @Test
    @JsName("userUrlIsIdentified")
    fun `User URL is identified`() {
        var type = downloader.getSourceType("https://www.reddit.com/user/mir_bby")
        assertTrue(type is SourceType.User, "URL is from a Reddit user")
        assertEquals("mir_bby", type.name, "Reddit username is 'mir_bby'")

        type = downloader.getSourceType("https://www.reddit.com/user/mir_bby/")
        assertTrue(type is SourceType.User, "URL is from a Reddit user")
        assertEquals("mir_bby", type.name, "Reddit username is 'mir_bby'")

        type = downloader.getSourceType("https://www.reddit.com/user/mir_bby/submitted/")
        assertTrue(type is SourceType.User, "URL is from a Reddit user")
        assertEquals("mir_bby", type.name, "Reddit username is 'mir_bby'")

        type = downloader.getSourceType("https://www.reddit.com/u/mir_bby")
        assertTrue(type is SourceType.User, "URL is from a Reddit user")
        assertEquals("mir_bby", type.name, "Reddit username is 'mir_bby'")

        type = downloader.getSourceType("https://www.reddit.com/u/mir_bby/")
        assertTrue(type is SourceType.User, "URL is from a Reddit user")
        assertEquals("mir_bby", type.name, "Reddit username is 'mir_bby'")

        type = downloader.getSourceType("https://www.reddit.com/u/mir_bby/submitted/")
        assertTrue(type is SourceType.User, "URL is from a Reddit user")
        assertEquals("mir_bby", type.name, "Reddit username is 'mir_bby'")

        type = downloader.getSourceType("https://www.reddit.com/r/pics/")
        assertFalse(type is SourceType.User, "URL is NOT from a Reddit user")
    }

    @Test
    @JsName("userSubmissionsAreProperlyFetched")
    fun `User submissions are properly fetched`() = runTest(timeout = 1.hours) {
        val response = downloader.queryMedia(
            "https://www.reddit.com/user/SerlianaElle/",
            Int.MAX_VALUE,
            emptyList(),
        )
        assertTrue(response.media.isNotEmpty())
    }
}