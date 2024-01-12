package io.vinicius.umd

import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.downloader.coomer.Coomer
import io.vinicius.umd.downloader.reddit.Reddit
import io.vinicius.umd.model.Metadata
import io.vinicius.umd.util.runBlocking
import kotlin.js.JsExport

@JsExport
fun queryMedia(url: String, limit: Int = 0, extensions: Array<String> = emptyArray()): Metadata {
    val downloader = findDownloader(url)
    return runBlocking { downloader.queryMedia(url, limit, extensions.toList()) }
}

private fun findDownloader(url: String): Downloader {
    return when {
        Coomer.isUrlMatch(url) -> Coomer()
        Reddit.isUrlMatch(url) -> Reddit()
        else -> throw IllegalArgumentException("URL not supported")
    }
}