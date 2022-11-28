package com.example.battleshipmobile.lobby

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.battleshipmobile.battleship.play.QueueActivity
import com.example.battleshipmobile.ui.TestTags
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QueueActivityTests {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.battleshipmobile", appContext.packageName)
    }

    @get:Rule
    val testRule = createAndroidComposeRule<QueueActivity>()

    @Test
    fun lobbyScreenIsDisplayed() {
        testRule.onNodeWithTag(TestTags.Lobby.Screen).assertExists()
        testRule.onNodeWithTag(TestTags.Lobby.LobbyState).assertExists()
        testRule.onNodeWithTag(TestTags.Lobby.LoadingIndicator).assertExists()
        testRule.onNodeWithTag(TestTags.Lobby.StatusMessage).assertExists()
    }

}

