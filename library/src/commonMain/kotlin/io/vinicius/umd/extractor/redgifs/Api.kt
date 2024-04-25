package io.vinicius.umd.extractor.redgifs

import io.vinicius.klopik.Klopik

internal class Api {
    private val klopik = Klopik("https://api.redgifs.com/")

    suspend fun getToken(): Auth {
        val res = klopik.get("v2/auth/temporary") {
            headers = mapOf(
                "Content-Type" to "application/json",
                "Origin" to "https://www.redgifs.com",
                "Referer" to "https://www.redgifs.com/",
                "User-Agent" to "UMD"
            )
        }.execute()

        return res.deserialize()
    }

    suspend fun getVideo(token: String, videoUrl: String, videoId: String): Watch {
        val res = klopik.get("v2/gifs/$videoId?views=yes") {
            headers = mapOf(
                "Authorization" to token,
                "X-CustomHeader" to videoUrl,
                "User-Agent" to "UMD"
            )
        }.execute()

        return res.deserialize()
    }
}