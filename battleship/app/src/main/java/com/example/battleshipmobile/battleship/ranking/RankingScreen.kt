package com.example.battleshipmobile.battleship.ranking

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.components.LazyTable
import com.example.battleshipmobile.battleship.service.ranking.PlayerStatisticsDTO
import com.example.battleshipmobile.battleship.service.ranking.StatisticsEmbedded
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.dismissKeyboard
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.theme.HEADER_COLOR
import com.example.battleshipmobile.ui.views.general.BackButton
import kotlinx.coroutines.launch

@Composable
fun RankingScreen(
    onBackClick : () -> Unit,
    gameStatistics : StatisticsEmbedded?,
    onSearchError : (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var userSearch by rememberSaveable { mutableStateOf("") }
    val tableLazyState = rememberLazyListState()

    BattleshipMobileTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .testTag(TestTags.Statistics.Screen)
        ) {
            if(gameStatistics != null){
                fun findIndexOf(name: String) : Int? =
                    gameStatistics.ranking.indexOfFirst { it.player == name }.let { i ->
                        if (i == -1) null else i
                    }

                StatelessRankingScreen(
                    onBackClick = onBackClick,
                    gameStatistics = gameStatistics,
                    userSearchValue = userSearch,
                    userSearchOnChange = { userSearch = it },
                    onTextSubmit = {
                        Log.d("RankingScreen", "onTextSubmit")
                        coroutineScope.launch {
                            Log.v("RankingScreen", "Searching for $userSearch")
                            findIndexOf(userSearch)?.let { index ->
                                Log.v("RankingScreen", "Found $userSearch at $index")
                                tableLazyState.animateScrollToItem(index)
                            } ?: onSearchError(userSearch)
                        }
                    },
                    tableLazyState = tableLazyState,
                )
            } else{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

        }
    }
}

@Composable
private fun StatelessRankingScreen(
    onBackClick : () -> Unit,
    gameStatistics : StatisticsEmbedded,
    userSearchValue : String,
    userSearchOnChange : (String) -> Unit,
    onTextSubmit : () -> Unit = {},
    tableLazyState : LazyListState,
    focusManager: FocusManager = LocalFocusManager.current,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    dismissKeyboard(focusManager)
                })
            },
    ) {


        Text(
            text = stringResource(id = R.string.ranking_label),
            color = HEADER_COLOR,
            fontSize = MaterialTheme.typography.h3.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)

        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = stringResource(R.string.total_games_played_label) + ": ${gameStatistics.nGames}")

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
        ) {

            SearchComponent(
                userSearchValue = userSearchValue,
                userSearchOnChange = userSearchOnChange,
                onTextSubmit = onTextSubmit,
                focusManager = focusManager
            )

            LazyTable(
                headers = listOf(
                    stringResource(R.string.rank_label),
                    stringResource(R.string.player_label),
                    stringResource(R.string.games_label),
                    stringResource(R.string.wins_label)
                ),
                cellsWeight = listOf(3f, 4f, 2f, 2f),
                playerStats = gameStatistics.ranking,
                modifier = Modifier.testTag(TestTags.Statistics.Table),
                dataListState = tableLazyState,
                height = 550.dp,
                width = 350.dp,
            )
    }
        BackButton(onBackClick = onBackClick)
    }


}


@Composable
private fun SearchComponent(
    userSearchValue : String,
    userSearchOnChange : (String) -> Unit,
    onTextSubmit : () -> Unit,
    focusManager: FocusManager,
) {
    Row(
        modifier = Modifier.
        height(75.dp)
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight(0.85f)
                .padding(end = 2.dp)
        ){
            OutlinedTextField(
                value = userSearchValue,
                onValueChange = userSearchOnChange,
                singleLine = true,
                label = { Text(stringResource(R.string.search_player_label)) },
                keyboardActions = KeyboardActions(
                    onDone = { dismissKeyboard(focusManager) }
                ),
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxHeight()
                    .testTag(TestTags.Statistics.SearchField)
            )
        }

        Button(
            onClick = onTextSubmit,
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(0.1f)
                .align(Alignment.CenterVertically)
                .fillMaxHeight(0.6f)
                .testTag(TestTags.Statistics.SearchButton)
        ) {
            Text(text = stringResource(R.string.search_button),maxLines = 1)
        }
    }
}



private val gameStats = StatisticsEmbedded(24,
    listOf(
        PlayerStatisticsDTO(1, "pedro", 6, 1),
        PlayerStatisticsDTO(2, "joao", 10, 4),
        PlayerStatisticsDTO(3, "miguel", 100, 1),
        PlayerStatisticsDTO(4, "tiago", 2, 21),
        PlayerStatisticsDTO(5, "veloso", 2, 111),
        PlayerStatisticsDTO(6, "benfica_Grande", 2, 1),
        PlayerStatisticsDTO(7, "nome grande para ver se funciona bem", 2, 1),
        PlayerStatisticsDTO(8, "rui", 2, 1),
        PlayerStatisticsDTO(9, "usernameGenerico", 2, 1),
        PlayerStatisticsDTO(10, "utilizadorxpto", 2, 1),
        PlayerStatisticsDTO(11, "utilizadorxpto24212", 2, 1),
        )
)

@Preview(showBackground = true)
@Composable
fun StatelessRankingScreenPreview() {
    BattleshipMobileTheme {
        StatelessRankingScreen(
            onBackClick = {},
            gameStatistics = gameStats,
            userSearchValue = "",
            userSearchOnChange = {},
            onTextSubmit = {},
            tableLazyState = rememberLazyListState(),
            focusManager = LocalFocusManager.current,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    BattleshipMobileTheme {
        RankingScreen(
            onBackClick = {},
            gameStatistics = gameStats,
            onSearchError = {}
        )
    }
}