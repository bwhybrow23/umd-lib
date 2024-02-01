package io.vinicius.umd.util

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal interface Contract {
    @GET("")
    suspend fun getString(@Url url: String): Response<String>
}

data class FetchException(
    val statusCode: Int,
    override val message: String
): Exception()

class Fetch {
    private val ktorfit = Ktorfit.Builder()
        .build()
        .create<Contract>()

    fun getFlow(url: String, retries: Int = 0, duration: Duration = 15.seconds): Flow<String> {
        var retryCount = 0

        return flow {
            val response = ktorfit.getString(url)

            if (response.isSuccessful) {
                emit(response.body().orEmpty())
            } else {
                throw FetchException(response.code, response.message)
            }
        }.retry {
            if (retries > 0) {
                delay(duration)
                retryCount++
                retryCount <= retries
            } else {
                false
            }
        }
    }

    companion object {
        internal val jsonClient = Ktorfit.Builder()
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