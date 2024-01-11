package io.vinicius.umd.downloader.reddit

object Util {
    fun getSourceType(url: String): SourceType {
        val regexUser = """/(?:user|u)/([^/?]+)""".toRegex()
        val regexSubreddit = """/r/([^/?]+)""".toRegex()

        return when {
            url.contains(regexUser) -> {
                val match = regexUser.find(url)
                SourceType.User(match?.groupValues?.get(1).orEmpty())
            }

            url.contains(regexSubreddit) -> {
                val match = regexSubreddit.find(url)
                SourceType.Subreddit(match?.groupValues?.get(1).orEmpty())
            }

            else -> SourceType.Unknown
        }
    }
}