package com.example.battleshipmobile.battleship.http.hypermedia

import okhttp3.MediaType.Companion.toMediaType


/**
 * For details regarding the Problem media type,
 * @see <a href="https://www.rfc-editor.org/rfc/rfc7807">Problem Details for HTTP APIs</a>
 */
val ProblemMediaType = "application/problem+json".toMediaType()

/**
 * Represents a problem as they are represented in the Problem media type.
 */
data class Problem(
    val type : String? = null,
    val title : String? = null,
    val detail : String? = null,
    val instance : String? = null,
): Throwable()