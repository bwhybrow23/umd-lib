package io.vinicius.umd.ktx

import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.encodedPath

/**
 * Remove the query parameters from a URL.
 */
fun Url.cleanUrl(): String {
    return URLBuilder().apply {
        protocol = this@cleanUrl.protocol
        host = this@cleanUrl.host
        port = this@cleanUrl.port
        encodedPath = this@cleanUrl.encodedPath
    }.buildString()
}