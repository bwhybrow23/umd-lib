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
    /**
     * Data class representing the progress of a download operation.
     *
     * @property bytes The number of bytes that have been downloaded so far.
     * @property total The total number of bytes that need to be downloaded.
     */
    data class OnProgress(val bytes: Long, val total: Long) : DownloadStatus()

    /**
     * Data class representing the completion of a download operation.
     *
     * @property total The total number of bytes that were downloaded.
     */
    data class OnComplete(val total: Long) : DownloadStatus()
}

private typealias DownloadCallback = (DownloadStatus) -> Unit

/**
 * Fetch class is used to send HTTP requests.
 *
 * @param headers A map of headers to be included in the HTTP request. Default is an empty map.
 * @param retries The number of times to retry the request in case of failure. Default is 0.
 */
class Fetch(
    private val headers: Map<String, String> = emptyMap(),
    private val retries: Int = 0
) {
    private val tag = "Fetch"

    private val klopik = Klopik {
        headers = this@Fetch.headers
        retries = this@Fetch.retries
        retryWhen = { req, res, attempt ->
            if (res.statusCode.toInt() == 429) {
                Logger.d(tag) { "#$attempt Retrying request: ${req.url}" }
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
        val path = filePath.toPath()
        var total = 0L

        fileSystem.sink(path).buffer().use { file ->
            klopik.get(url).stream { chunk ->
                val size = chunk.size.toLong()
                total += size
                file.write(chunk)

                callback?.invoke(DownloadStatus.OnProgress(size, total))
            }
        }

        callback?.invoke(DownloadStatus.OnComplete(total))
    }
}