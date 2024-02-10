package io.vinicius.umd.extractor

import io.vinicius.umd.model.Response
import io.vinicius.umd.util.Fetch

internal interface Extractor {
    suspend fun queryMedia(url: String, limit: Int, extensions: List<String>): Response
    fun configureFetch(): Fetch
}