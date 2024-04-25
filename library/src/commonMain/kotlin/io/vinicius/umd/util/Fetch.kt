package io.vinicius.umd.util

import co.touchlab.kermit.Logger
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import io.vinicius.klopik.Klopik
import io.vinicius.klopik.exception.KlopikException
import io.vinicius.umd.exception.FetchException
import kotlinx.coroutines.delay
import okio.Path.Companion.toPath
import okio.buffer
import okio.use
import kotlin.math.pow
import kotlin.time.Duration.Companion.seconds

sealed class DownloadStatus {
    data class OnProgress(val bytes: Long, val total: Long) : DownloadStatus()
    data class OnComplete(val total: Long) : DownloadStatus()
}

private typealias DownloadCallback = (DownloadStatus) -> Unit

/**
 * Fetch class is used to send HTTP requests.
 *
 * @param headers A map of headers to be included in the HTTP request. Default is an empty map.
 * @param retries The number of times to retry the request in case of failure. Default is 0.
 * @param followRedirects A boolean indicating whether to follow redirects or not.
 */
class Fetch(
    private val headers: Map<String, String> = emptyMap(),
    private val retries: Int = 0,
    followRedirects: Boolean = true,
) {
    private val tag = "Fetch"

    private val klopik = Klopik {
        headers = this@Fetch.headers
        retries = this@Fetch.retries
        retryWhen = { res, attempt ->
            if (res.statusCode.toInt() == 429) {
                Logger.d(tag) { "#$attempt Retrying request..." }
                val wait = 2.0.pow(attempt.toDouble()).toLong()
                delay(wait.seconds)
                true
            } else {
                false
            }
        }
    }

    /**
     * Send a GET request to a URL and returns the response as string.
     *
     * @param url the URL that will receive the request.
     * @return the response represented as `String`.
     * @throws FetchException
     */
    suspend fun getString(url: String): String {
        @Suppress("SwallowedException")
        return try {
            klopik.get(url).execute().text
        } catch (e: KlopikException) {
            throw FetchException(e.response?.statusCode?.toInt() ?: 0, e.response?.text.orEmpty())
        }
    }

    /**
     * Downloads a file.
     *
     * @param url the URL where the file is located.
     * @param filePath the path where the file will be saved.
     * @param callback lambda that tracks the download progress.
     */
    @DefaultArgumentInterop.Enabled
    suspend fun downloadFile(url: String, filePath: String, callback: DownloadCallback? = null) {
        var total = 0L

        klopik.get(url).stream { chunk ->
            val path = filePath.toPath()
            val size = chunk.size.toLong()
            total += size

            callback?.invoke(DownloadStatus.OnProgress(size, total))
            fileSystem.sink(path).buffer().use { it.write(chunk) }
        }

        callback?.invoke(DownloadStatus.OnComplete(total))
    }
}