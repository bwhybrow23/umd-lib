package io.vinicius.umd.exception

import io.vinicius.umd.model.ExtractorType

data class InvalidSourceException(
    val url: String,
    val extractor: ExtractorType,
    override val message: String,
) : Exception()