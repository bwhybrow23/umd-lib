package io.vinicius.umd.extractor.redgifs

internal sealed class SourceType {
    data class Video(val id: String) : SourceType()
}