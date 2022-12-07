package com.example.battleshipmobile.ui.views.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.user.Password

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = Password.strengthLevel(password)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.strength_indicator_label),
            color = Color.Black,
            fontSize = 16.sp
        )
        for(idx in StrengthLevel.values().indices){
            val color = if(idx <= strength.ordinal) strength.color else Color.Transparent
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(40.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(50))
                    .background(color, RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PasswordStrengthVeryStrongPreview() {
    PasswordStrengthIndicator(password = "costakilapada!D")
}

@Preview(showBackground = true)
@Composable
private fun PasswordMinStrengthStrongPreview() {
    PasswordStrengthIndicator(password = "costakilapada!")
}

@Preview(showBackground = true)
@Composable
private fun PasswordMinStrengthDecentPreview() {
    PasswordStrengthIndicator(password = "costakilapada")
}

@Preview(showBackground = true)
@Composable
private fun PasswordMinStrengthWeakPreview() {
    PasswordStrengthIndicator(password = "costa")
}

@Preview(showBackground = true)
@Composable
private fun PasswordMinStrengthEmptyPreview() {
    PasswordStrengthIndicator(password = "")
}