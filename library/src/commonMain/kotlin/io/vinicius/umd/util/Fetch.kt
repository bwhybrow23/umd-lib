package io.vinicius.umd.util

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal interface Contract {
    @GET("")
    suspend fun getString(
        @Url url: String
    ): String
}

class Fetch : Contract {
    private val ktorfit = Ktorfit.Builder()
        .build()
        .create<Contract>()

    override suspend fun getString(url: String) = ktorfit.getString(url)

    companion object {
        internal val jsonClient = Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    })
                }
            }
    }
}