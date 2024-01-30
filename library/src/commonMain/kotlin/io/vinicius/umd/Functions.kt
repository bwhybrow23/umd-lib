package io.vinicius.umd

import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.extractor.coomer.Coomer
import io.vinicius.umd.extractor.reddit.Reddit

internal fun findExtractor(url: String): Extractor {
    return when {
        Coomer.isMatch(url) -> Coomer()
        Reddit.isMatch(url) -> Reddit()
        else -> throw IllegalArgumentException("URL not supported")
    }
}