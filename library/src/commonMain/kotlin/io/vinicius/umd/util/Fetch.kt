package io.vinicius.umd.util

import co.touchlab.kermit.Logger
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.DEFAULT_HTTP_BUFFER_SIZE
import io.ktor.http.contentLength
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import okio.buffer

data class FetchException(
    val statusCode: Int,
    override val message: String,
) : Exception()

sealed class DownloadStatus {
    data class OnProgress(val bytes: Long, val total: Long) : DownloadStatus()
    data class OnComplete(val total: Long) : DownloadStatus()
}

private typealias DownloadCallback = (DownloadStatus) -> Unit

class Fetch {
    private val ktorfit = Ktorfit.Builder()
        .httpClient {
            install(HttpRequestRetry) {
                maxRetries = 5
                retryIf { req, res ->
                    val shouldRetry = res.status.value == 429
                    if (shouldRetry) Logger.d("Fetch") { "#$retryCount Retrying request: ${req.url}" }
                    shouldRetry
                }
                exponentialDelay()
            }
        }
        .build()

    /**
     * Send a GET request to a URL and returns the response as string.
     *
     * @param url the URL that will receive the request.
     * @return the response represented as `String`.
     * @throws FetchException
     */
    suspend fun getString(url: String): String {
        val response = ktorfit.httpClient.get(url)

        return if (response.status.value in 200..299) {
            response.bodyAsText()
        } else {
            throw FetchException(response.status.value, response.bodyAsText())
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
        ktorfit.httpClient.prepareGet(url).execute { httpResponse ->
            val fileSize = httpResponse.contentLength() ?: 0
            val channel = httpResponse.body<ByteReadChannel>()
            val path = filePath.toPath()
            val sink = fileSystem.sink(path).buffer()

            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(DEFAULT_HTTP_BUFFER_SIZE.toLong())
                callback?.invoke(DownloadStatus.OnProgress(channel.totalBytesRead, fileSize))

                while (packet.isNotEmpty) {
                    val bytes = packet.readBytes()
                    sink.write(bytes)
                }
            }

            callback?.invoke(DownloadStatus.OnComplete(fileSize))
        }
    }

    companion object {
        internal val ktorJson = Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
            }
    }
}