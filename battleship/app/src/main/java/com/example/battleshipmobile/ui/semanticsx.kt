package com.example.battleshipmobile.ui

import androidx.compose.ui.semantics.SemanticsPropertyKey
import com.example.battleshipmobile.battleship.auth.AuthenticationFormType



val authType = SemanticsPropertyKey<AuthenticationFormType>("authType")

/**
 * This is a semantic property that is used to identify if a password field content is visible or not.
 */
val contentIsVisible = SemanticsPropertyKey<Boolean>("isVisible")

/**
 * This is a semantic property that is used to identify if a password field is in error state or not.
 */
val IsError = SemanticsPropertyKey<Boolean>("isError")