package com.example.battleshipmobile.ui.views

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.battleshipmobile.ui.TestTags

@Composable
fun PasswordField(
    value: String,
    label: String,
    isVisible: Boolean,
    isError: Boolean,
    error: String,
    onValueChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextFieldValidation(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        placeholder = { Text(label) },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (isVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (isVisible) "Hide password" else "Show password"

            IconButton(onClick = onVisibilityToggle, modifier = Modifier.testTag(TestTags.Other.PasswordVisibilityIcon)) {
                Icon(imageVector = image, description)
            }
        },
        modifier=modifier,
        error = error
    )
}