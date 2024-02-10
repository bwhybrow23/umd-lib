package io.vinicius.umd.extractor.redgifs

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path
import io.vinicius.umd.util.Fetch.Companion.ktorJson

internal interface Contract {
    @Headers(
        "Content-Type: application/json",
        "Origin: https://www.redgifs.com",
        "Referer: https://www.redgifs.com/",
        "User-Agent: UMD",
    )
    @GET("v2/auth/temporary")
    suspend fun getToken(): Auth

    @Headers("User-Agent: UMD")
    @GET("v2/gifs/{videoId}?views=yes")
    suspend fun getVideo(
        @Header("Authorization") token: String,
        @Header("X-CustomHeader") videoUrl: String,
        @Path("videoId") videoId: String,
    ): Watch
}

internal class RedgifsApi : Contract {
    private val api = ktorJson
        .baseUrl("https://api.redgifs.com/")
        .build()
        .create<Contract>()

    override suspend fun getToken(): Auth = api.getToken()

    override suspend fun getVideo(token: String, videoUrl: String, videoId: String) =
        api.getVideo(token, videoUrl, videoId)
}