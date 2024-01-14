package io.vinicius.umd

import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.downloader.coomer.Coomer
import io.vinicius.umd.downloader.reddit.Reddit
import io.vinicius.umd.model.EventCallback

internal fun findDownloader(url: String, event: EventCallback?): Downloader {
    return when {
        Coomer.isMatch(url) -> Coomer()
        Reddit.isMatch(url) -> Reddit(event)
        else -> throw IllegalArgumentException("URL not supported")
    }
}