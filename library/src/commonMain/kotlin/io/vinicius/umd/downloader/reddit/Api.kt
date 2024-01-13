package io.vinicius.umd.downloader.reddit

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.vinicius.umd.util.Fetch.Companion.jsonClient

internal interface Contract {
    @GET("user/{user}/submitted.json?sort=new&raw_json=1")
    suspend fun getUserSubmissions(
        @Path("user") user: String,
        @Query("after") after: String,
        @Query("limit") limit: Int,
    ): Submission
}

internal class RedditApi : Contract {
    private val api = jsonClient
        .baseUrl("https://www.reddit.com/")
        .build()
        .create<Contract>()

    override suspend fun getUserSubmissions(user: String, after: String, limit: Int) =
        api.getUserSubmissions(user, after, limit)
}