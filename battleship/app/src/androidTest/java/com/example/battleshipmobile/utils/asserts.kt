package com.example.battleshipmobile.utils

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import com.example.battleshipmobile.battleship.auth.AuthenticationFormType
import com.example.battleshipmobile.ui.IsError
import com.example.battleshipmobile.ui.authType
import com.example.battleshipmobile.ui.contentIsVisible

fun SemanticsNodeInteraction.assertContentIsVisible() {
    assert(SemanticsMatcher.expectValue(contentIsVisible, true))
}

fun SemanticsNodeInteraction.assertContentIsNotVisible() {
    assert(SemanticsMatcher.expectValue(contentIsVisible, false))
}

fun SemanticsNodeInteraction.assertFormType(formType: AuthenticationFormType) {
    assert(SemanticsMatcher.expectValue(authType, formType))
}

fun SemanticsNodeInteraction.assertIsError() {
    assert(SemanticsMatcher.expectValue(IsError, true))
}

fun SemanticsNodeInteraction.assertIsNotError() {
    assert(SemanticsMatcher.expectValue(IsError, false))
}