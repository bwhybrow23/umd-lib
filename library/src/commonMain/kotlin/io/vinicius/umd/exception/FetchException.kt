package io.vinicius.umd.exception

data class FetchException(
    val statusCode: Int,
    override val message: String,
) : Exception()