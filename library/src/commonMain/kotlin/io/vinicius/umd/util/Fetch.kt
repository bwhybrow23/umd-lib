package io.vinicius.umd.util

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

data class FetchException(
    val statusCode: Int,
    override val message: String
): Exception()

class Fetch {
    private val ktorfit = Ktorfit.Builder()
        .httpClient {
            install(HttpRequestRetry) {
                maxRetries = 5
                retryIf { _, res -> res.status.value == 429 }
                exponentialDelay()
            }
        }
        .build()

    /**
     * Send a GET request to a URL and returns a response as string.
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