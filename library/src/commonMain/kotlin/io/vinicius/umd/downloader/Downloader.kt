package io.vinicius.umd.downloader

import io.vinicius.umd.model.Response

interface Downloader {
    suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response
}