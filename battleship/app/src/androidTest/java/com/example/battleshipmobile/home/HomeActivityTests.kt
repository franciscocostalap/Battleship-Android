package com.example.battleshipmobile.home

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.utils.PreserveDefaultDependencies
import com.example.battleshipmobile.utils.createPreserveDefaultDependenciesComposeRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private val mockRepo: AuthInfoService = mockk(relaxed = true) {
        every { uid } returns null
    }

    @Test
    fun expected_home_screen_without_auth_info() {

        // Arrange
        application.authInfoService = mockRepo


        ActivityScenario.launch(HomeActivity::class.java).use {

            // Assert
            testRule.onNodeWithTag(TestTags.Home.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Home.SignInButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.RankingsButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.CreditsButton).assertExists()
        }

    }



    @Test
    fun pressing_sign_in_button_navigates_to_login_activity(){

        // Arrange
        application.authInfoService = mockRepo

        ActivityScenario.launch(HomeActivity::class.java).use{

            testRule.onNodeWithTag(TestTags.Home.SignInButton).performClick()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Auth.Screen).assertExists()
        }

    }



}