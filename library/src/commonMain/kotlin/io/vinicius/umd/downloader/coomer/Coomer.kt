package io.vinicius.umd.downloader.coomer

import io.ktor.http.Url
import io.vinicius.umd.downloader.Downloader
import io.vinicius.umd.model.Response

class Coomer : Downloader {
    override suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response {
        TODO("Not yet implemented")
    }

    companion object {
        fun isMatch(url: String): Boolean {
            val urlObj = Url(url)
            return urlObj.host.endsWith("coomer.su", true)
        }
    }
}