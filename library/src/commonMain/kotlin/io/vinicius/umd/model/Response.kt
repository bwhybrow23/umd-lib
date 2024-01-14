package io.vinicius.umd.model

data class Response(
    val url: String,
    val media: List<Media>,
    val downloader: DownloaderType,
)