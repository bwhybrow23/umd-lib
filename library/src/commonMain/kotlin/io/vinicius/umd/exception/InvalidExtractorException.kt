package io.vinicius.umd.exception

data class InvalidExtractorException(
    val url: String,
    override val message: String,
) : Exception()