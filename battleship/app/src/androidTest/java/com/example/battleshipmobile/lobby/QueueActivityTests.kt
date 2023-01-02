package com.example.battleshipmobile.lobby


import android.content.Intent
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.battleship.play.lobby.QueueActivity
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.utils.PreserveDefaultDependencies
import com.example.battleshipmobile.utils.createPreserveDefaultDependenciesComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class QueueActivityTests {
    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    @Test
    fun lobby_screen_is_displayed() {
        val intent = createIntent(lobbyID = 0)
        ActivityScenario.launch<QueueActivity>(intent).use{
            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.LoadingIndicator).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.LoadingIndicator).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.StatusMessage).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.CancelButton).assertExists()
        }
    }

    @Test
    fun queue_screen_navigates_to_home_activity_after_click_of_player1_on_cancel_button() {
        val intent = createIntent(lobbyID = 0)
        ActivityScenario.launch<QueueActivity>(intent).use {
            testRule.onNodeWithTag(TestTags.Lobby.CancelButton).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertDoesNotExist()
        }
    }

    @Test
    fun queue_screen_navigates_to_home_activity_after_click_of_player1_on_back_button() {
       val intent = createIntent(lobbyID = 0)

        ActivityScenario.launch<QueueActivity>(intent).use {
            it.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertDoesNotExist()
        }
    }

    @Test
    fun queue_screen_does_not_navigate_to_home_activity_after_click_of_player2_on_back_button() {
        val intent = createIntent(lobbyID = 0, gameID = 0)

        ActivityScenario.launch<QueueActivity>(intent).use {
            testRule.onNodeWithTag(TestTags.Lobby.CancelButton).assertDoesNotExist()
            it.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }
            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
        }
    }

    @Test
    fun queue_state_waiting_survives_reconfiguration() {
        val intent = createIntent(lobbyID = 0)

        ActivityScenario.launch<QueueActivity>(intent).use {
            testRule.waitForIdle()
            it.recreate()

            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.LoadingIndicator).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.StatusMessage).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.CancelButton).assertExists()
        }
    }

    @Test
    fun queue_state_full_survives_reconfiguration() {
        val intent = createIntent(lobbyID = 0, gameID = 0)

        ActivityScenario.launch<QueueActivity>(intent).use {
            testRule.waitForIdle()
            it.recreate()

            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.LoadingIndicator).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.StatusMessage).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.CancelButton).assertDoesNotExist()
        }
    }

    private fun createIntent(lobbyID: ID, gameID: ID? = null): Intent{
        return Intent(application, QueueActivity::class.java).also {
            it.putExtra(QueueActivity.LOBBY_ID_EXTRA, lobbyID)
            gameID ?: return@also
            it.putExtra(QueueActivity.GAME_ID_EXTRA, gameID)
        }
    }
}

