package com.example.battleshipmobile.battleship.info
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

data class SocialInfo(val name: String, val nr: Int, val email: String)

@Composable
fun InfoScreen(
    onSendEmailRequested: () -> Unit,
    socials: List<SocialInfo>
) {
    Surface(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        StatelessInfoScreen(
            onSendEmailRequested = onSendEmailRequested,
            socials = socials,
        )
    }
}

@Composable
private fun StatelessInfoScreen(
    onSendEmailRequested: () -> Unit = { },
    socials: List<SocialInfo> = emptyList()
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            repeat(socials.size){
                val author = socials[it]
                Text(text = "${author.name} - ${author.nr}", style = MaterialTheme.typography.h5)
            }
            Box( modifier = Modifier.clickable { onSendEmailRequested()}) {
                Icon(imageVector = Icons.Default.Email, contentDescription = null)
            }

        }
    }
}

@Preview
@Composable
fun InfoScreenPreview(){
    StatelessInfoScreen(
        onSendEmailRequested =  { },
        socials = emptyList()
    )
}
