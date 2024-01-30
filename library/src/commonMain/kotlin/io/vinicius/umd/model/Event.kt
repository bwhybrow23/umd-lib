package io.vinicius.umd.model

sealed class Event {
    data class OnExtractorFound(val name: String) : Event()
    data class OnExtractorTypeFound(val type: String, val name: String? = null) : Event()
    data class OnMediaQueried(val amount: Int) : Event()
    data class OnQueryCompleted(val total: Int) : Event()
}