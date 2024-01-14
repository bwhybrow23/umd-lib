package io.vinicius.umd.downloader.reddit

import io.vinicius.umd.downloader.DownloaderType
import io.vinicius.umd.model.Media
import io.vinicius.umd.model.Response

data class RedditResponse(
    override val url: String,
    override val media: Array<Media>,
    override val downloader: DownloaderType = DownloaderType.Reddit,
    val source: SourceType,
) : Response