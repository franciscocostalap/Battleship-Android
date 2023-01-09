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
    fun queue_state_waiting_survives_reconfiguration() {
        val intent = createIntent()

        ActivityScenario.launch<QueueActivity>(intent).use {
            testRule.waitForIdle()
            it.recreate()

            testRule.onNodeWithTag(TestTags.Lobby.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.LoadingIndicator).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.StatusMessage).assertExists()
            testRule.onNodeWithTag(TestTags.Lobby.CancelButton).assertExists()
        }
    }


    private fun createIntent(): Intent{
        return Intent(application, QueueActivity::class.java)
    }
}

