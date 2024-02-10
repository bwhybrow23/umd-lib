package io.vinicius.umd.ktx

import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.encodedPath

/**
 * Remove the query parameters from a URL.
 */
internal val Url.cleanUrl: String get() {
    return URLBuilder().apply {
        protocol = this@cleanUrl.protocol
        host = this@cleanUrl.host
        port = this@cleanUrl.port
        encodedPath = this@cleanUrl.encodedPath
    }.buildString()
}

/**
 * Return the file extension, if any, present in the URL.
 */
internal val Url.extension: String? get() {
    return encodedPath
        .substringAfterLast(".", "")
        .lowercase()
        .ifEmpty { null }
}