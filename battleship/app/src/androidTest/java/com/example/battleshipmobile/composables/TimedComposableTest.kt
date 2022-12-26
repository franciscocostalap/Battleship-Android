package com.example.battleshipmobile.composables

import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.ui.views.general.TimedComposable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimedComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTimedComposable() {

        composeTestRule.mainClock.autoAdvance = false
        try{
            composeTestRule.setContent {
                TimedComposable(1000, onTimeout={  }) {
                    Text(text = "Hello World", modifier= Modifier.testTag("HelloWorld"))
                }
            }

            composeTestRule.onNodeWithTag("HelloWorld").assertExists()
            composeTestRule.mainClock.advanceTimeBy(1100)
            composeTestRule.onNodeWithTag("HelloWorld").assertDoesNotExist()
        }finally {
            composeTestRule.mainClock.autoAdvance = true
        }
    }

}