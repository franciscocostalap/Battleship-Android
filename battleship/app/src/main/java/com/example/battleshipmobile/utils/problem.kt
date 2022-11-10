package com.example.battleshipmobile.utils

import okhttp3.MediaType.Companion.toMediaType
import java.net.URI

val ProblemMediaType = "application/problem+json".toMediaType()

/**
 *
 */
data class Problem(
    val type : URI? = null,
    val title : String? = null,
    val detail : String? = null,
    val instance : String? = null,
)
