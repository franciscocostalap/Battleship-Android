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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.system_info.SystemInfo
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.general.BackButton


@Composable
fun InfoScreen(
    onBackClick: () -> Unit,
    onSendClick: (email: String) -> Unit,
    isLoading : Boolean ,
    info : SystemInfo?
) {
    BattleshipMobileTheme {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .testTag(TestTags.Info.Screen)
        ) {

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
               require(info != null) { "System info is null" }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.info_title),
                        style = MaterialTheme.typography.h3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.authors),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    // Card for each social
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(info.authors) { social ->
                            SocialCard(
                                social = social,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                onClick = { onSendClick(social.email) }
                            )
                            if (social != info.authors.last())
                                Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.version_text) + " " + info.version,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )


                    Box(
                        modifier= Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ){
                        BackButton(
                            onBackClick = onBackClick,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }

                }
            }
        }

    }
}



@Composable
fun SocialCard(
    social: SystemInfo.Author,
    modifier: Modifier = Modifier,
    height : Dp = 100.dp,
    contentColor : Color = MaterialTheme.colors.primaryVariant,

    onClick: () -> Unit = {},

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier= Modifier
                .fillMaxSize()
                .background(contentColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${social.name} ${social.iselID}",
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
    SystemInfo.Author(
        name = "Francisco Costa",
        iselID = 48282,
        email =  "a48282@alunos.isel.pt",
        github = ""
    ),
    SystemInfo.Author(
        name = "Tiago Filipe",
        iselID = 48265,
        email = "a48265@alunos.isel.pt",
        github = ""
    ),
    SystemInfo.Author(
        name = "Teodosie Pienescu",
        iselID = 48267,
        email = "a48267@alunos.isel.pt",
        github = ""
    )
)

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview(){
    InfoScreen(
        onBackClick = {},
        onSendClick = {},
        info = SystemInfo(
            version = "1.0",
            authors = socialsPreview
        ),
        isLoading = false
    )
}
