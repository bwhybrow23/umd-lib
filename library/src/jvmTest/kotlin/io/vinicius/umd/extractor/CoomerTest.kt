package io.vinicius.umd.extractor

import io.vinicius.umd.extractor.coomer.Coomer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CoomerTest {
    private val extractor = Coomer()

    @Test
    fun `Coomer URLs are identified by the downloader`() {
        var url = "https://coomer.su/onlyfans/user/belledelphine"
        assertTrue(Coomer.isMatch(url), "Coomer URL without www. matches")

        url = "https://www.coomer.su/onlyfans/user/belledelphine"
        assertTrue(Coomer.isMatch(url), "Coomer URL with www. matches")

        url = "https://www.google.com"
        assertFalse(Coomer.isMatch(url), "Google doesn't match Coomer URL")
    }

//    @Test
//    fun `User URL is identified`() {
//        var type = extractor.getSourceType("https://coomer.su/onlyfans/user/belledelphine")
//        assertTrue(type is SourceType.User, "URL is from a Coomer user")
//        assertEquals("belledelphine", type.user, "Coomer username is 'belledelphine'")
//
//        type = extractor.getSourceType("https://coomer.su/onlyfans/user/belledelphine/")
//        assertTrue(type is SourceType.User, "URL is from a Coomer user")
//        assertEquals("belledelphine", type.user, "Coomer username is 'belledelphine'")
//
//        type = extractor.getSourceType("https://coomer.su/onlyfans/user/belledelphine?o=50")
//        assertTrue(type is SourceType.User, "URL is from a Coomer user")
//        assertEquals("belledelphine", type.user, "Coomer username is 'belledelphine'")
//
//        type = extractor.getSourceType("https://coomer.su/artists/updated")
//        assertFalse(type is SourceType.User, "URL is NOT from a Coomer user")
//    }
}