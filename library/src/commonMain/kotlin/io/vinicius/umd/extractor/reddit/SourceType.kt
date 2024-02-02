package io.vinicius.umd.extractor.reddit

internal sealed class SourceType {
    data class User(val name: String) : SourceType()
    data class Subreddit(val name: String) : SourceType()
}