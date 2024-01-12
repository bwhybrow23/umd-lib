package io.vinicius.umd.downloader.reddit.model

import io.vinicius.umd.downloader.DownloaderType
import io.vinicius.umd.downloader.reddit.SourceType
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Metadata

data class RedditMetadata(
    override val url: String,
    override val media: Array<Media>,
    override val downloader: DownloaderType = DownloaderType.Reddit,
    val source: SourceType
) : Metadata