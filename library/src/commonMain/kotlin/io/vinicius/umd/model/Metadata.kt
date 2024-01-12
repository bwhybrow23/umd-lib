package io.vinicius.umd.model

import io.vinicius.umd.downloader.DownloaderType
import kotlin.js.JsExport

@JsExport
interface Metadata {
    val url: String
    val media: Array<Media>
    val downloader: DownloaderType
}
