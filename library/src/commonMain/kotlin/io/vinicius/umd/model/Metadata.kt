package io.vinicius.umd.model

import io.vinicius.umd.downloader.DownloaderType

interface Metadata {
    val url: String
    val media: List<Media>
    val downloader: DownloaderType
}
