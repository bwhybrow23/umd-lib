package io.vinicius.umd.extractor

import io.vinicius.umd.model.Response

internal interface Extractor {
    suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response
}