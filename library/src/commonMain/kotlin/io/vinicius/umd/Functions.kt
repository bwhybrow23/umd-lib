package io.vinicius.umd

import io.vinicius.umd.extractor.Extractor
import io.vinicius.umd.extractor.coomer.Coomer
import io.vinicius.umd.extractor.reddit.Reddit
import io.vinicius.umd.model.EventCallback

internal fun findExtractor(url: String, event: EventCallback?): Extractor {
    return when {
        Coomer.isMatch(url) -> Coomer()
        Reddit.isMatch(url) -> Reddit(event)
        else -> throw IllegalArgumentException("URL not supported")
    }
}