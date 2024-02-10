package io.vinicius.umd

import io.vinicius.umd.util.Fetch

suspend fun main() {
    val umd = Umd("https://www.redgifs.com/watch/lemonchiffoninternationalaffenpinscher")
    val response = umd.queryMedia(100)
    val url = response.media.first().url

    val fetch = Fetch()
    fetch.downloadFile(url, "/Users/vegidio/Desktop/test.mp4")
}