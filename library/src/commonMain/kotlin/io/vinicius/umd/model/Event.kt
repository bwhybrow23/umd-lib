package io.vinicius.umd.model

sealed class Event {
    data class OnDownloaderFound(val name: String) : Event()
    data class OnDownloadTypeFound(val type: String) : Event()
    data class OnMediaQueried(val amount: Int) : Event()
    data class OnQueryCompleted(val total: Int) : Event()
}

typealias EventCallback = (Event) -> Unit