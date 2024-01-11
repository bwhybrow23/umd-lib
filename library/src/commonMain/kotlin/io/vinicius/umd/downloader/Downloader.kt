package io.vinicius.umd.downloader

import io.vinicius.umd.model.Metadata

interface Downloader {
    fun isUrlMatch(url: String): Boolean
    fun fetchMedia(url: String, limit: Int = -1, extensions: List<String> = emptyList()): Metadata
}