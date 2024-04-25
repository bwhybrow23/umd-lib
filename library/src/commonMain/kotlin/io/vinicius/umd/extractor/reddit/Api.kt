package io.vinicius.umd.extractor.reddit

import io.vinicius.klopik.Klopik

internal class Api {
    private val klopik = Klopik("https://www.reddit.com/")

    suspend fun getSubmission(id: String): List<Submission> {
        val res = klopik.get("$id/.json?raw_json=1").execute()
        return res.deserialize()
    }

    suspend fun getUserSubmissions(user: String, after: String, limit: Int): Submission {
        val res = klopik.get("user/$user/submitted.json?sort=new&raw_json=1&after=$after&limit=$limit").execute()
        return res.deserialize()
    }

    suspend fun getSubredditSubmissions(subreddit: String, after: String, limit: Int): Submission {
        val res = klopik.get("r/$subreddit/hot.json?raw_json=1&after=$after&limit=$limit").execute()
        return res.deserialize()
    }
}