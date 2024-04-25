package io.vinicius.umd.model

sealed class Event {
    /**
     * A data class that represents an event when an extractor is found.
     * @property name The name of the extractor.
     */
    data class OnExtractorFound(val name: String) : Event()

    /**
     * A data class that represents an event when an extractor type is found.
     * @property type The type of the extractor.
     * @property name The name of the extractor.
     */
    data class OnExtractorTypeFound(val type: String, val name: String) : Event()

    /**
     * A data class that represents an event when media is queried.
     * @property amount The amount of media queried.
     */
    data class OnMediaQueried(val amount: Int) : Event()

    /**
     * A data class that represents an event when a query is completed.
     * @property total The total number of queries completed.
     */
    data class OnQueryCompleted(val total: Int) : Event()
}

/**
 * A typealias for a function that takes an Event as a parameter and returns Unit (void).
 * This can be used to simplify the declaration of variables and function signatures.
 */
typealias EventCallback = (event: Event) -> Unit