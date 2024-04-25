package io.vinicius.umd.ktx

import com.eygraber.uri.Url

/**
 * Remove the query parameters from a URL.
 */
internal val Url.cleanUrl: String get() {
    return this.buildUpon().apply {
        this.encodedQuery(null)
    }.build().toString()
}

/**
 * Return the file extension, if any, present in the URL.
 */
internal val Url.extension: String? get() {
    return encodedPath
        ?.substringAfterLast(".", "")
        ?.lowercase()
        ?.ifEmpty { null }
}