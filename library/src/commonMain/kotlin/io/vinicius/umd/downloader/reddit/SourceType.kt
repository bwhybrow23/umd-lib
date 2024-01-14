package io.vinicius.umd.downloader.reddit

sealed class SourceType {
    data class User(val name: String) : SourceType()
    data class Subreddit(val name: String) : SourceType()
    data object Unknown : SourceType()
}