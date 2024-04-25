package io.vinicius.umd.model

/**
 * Data class representing a response from a service.
 *
 * @property url The URL from which the response was obtained.
 * @property media A list of Media objects associated with the response.
 * @property extractor The type of extractor used to obtain the response.
 * @property metadata A map containing additional metadata about the response.
 */
data class Response(
    val url: String,
    val media: List<Media>,
    val extractor: ExtractorType,
    val metadata: Map<String, Any>,
)