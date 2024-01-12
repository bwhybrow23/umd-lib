package io.vinicius.umd.downloader

import io.vinicius.umd.model.Metadata

interface Downloader {
    suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Metadata
}