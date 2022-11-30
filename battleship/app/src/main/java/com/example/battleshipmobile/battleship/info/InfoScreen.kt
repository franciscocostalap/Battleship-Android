package com.example.battleshipmobile.battleship.info
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.NavigationHandlers
import com.example.battleshipmobile.ui.views.TopBar

data class SocialInfo(val name: String, val nr: Int, val email: String)

@Composable
fun InfoScreen(
    socials: List<SocialInfo>
) {
    BattleshipMobileTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .testTag(TestTags.Info.Screen),
            topBar = {
                TopBar(navigationHandlers = NavigationHandlers {})
                     },
        ) { padding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Card for each social
                    LazyColumn{
                        items(socials) { social ->
                            SocialCard(
                                social = social,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                            if(social != socials.last())
                                Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SocialCard(social: SocialInfo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier= Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${social.name} ${social.nr}",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Left,
                color=Color.White,
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Send email",
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
            )
        }
    }
}

val socialsPreview = listOf(
    SocialInfo(
        name = "Francisco Costa",
        nr = 48282,
        email =  "a48282@alunos.isel.pt"
    ),
    SocialInfo(
        name = "Tiago Filipe",
        nr = 48265,
        email = "a48265@alunos.isel.pt"
    ),
    SocialInfo(
        name = "Teodosie Pienescu",
        nr = 48267,
        email = "a48267@alunos.isel.pt"
    )
)

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview(){
    InfoScreen(
        socials = socialsPreview
    )
}
