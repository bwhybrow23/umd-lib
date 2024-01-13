package io.vinicius.umd

import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.downloader.coomer.Coomer
import io.vinicius.umd.downloader.reddit.Reddit
import io.vinicius.umd.model.Response
import io.vinicius.umd.util.runBlocking
import kotlin.js.JsExport

@JsExport
fun queryMedia(url: String, limit: Int = Int.MAX_VALUE, extensions: Array<String> = emptyArray()): Response {
    val downloader = findDownloader(url)
    return runBlocking { downloader.queryMedia(url, limit, extensions.toList()) }
}

private fun findDownloader(url: String): Downloader {
    return when {
        Coomer.isMatch(url) -> Coomer()
        Reddit.isMatch(url) -> Reddit()
        else -> throw IllegalArgumentException("URL not supported")
    }
}