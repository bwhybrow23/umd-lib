package io.vinicius.umd.extractor.coomer

internal sealed class SourceType {
    data class User(val service: String, val user: String) : SourceType()
    data class Post(val service: String, val user: String, val id: String) : SourceType()
}