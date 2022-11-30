package com.example.battleshipmobile.home

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.auth.AuthInfoRepository
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

    private val mockRepo: AuthInfoRepository = mockk(relaxed = true) {
        every { authInfo } returns null
    }

    @Test
    fun expected_home_screen_without_auth_info() {

        // Arrange
        application.authInfoRepository = mockRepo


        ActivityScenario.launch(HomeActivity::class.java).use {

            // Assert
            testRule.onNodeWithTag(TestTags.Home.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Home.SignInButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.RankingsButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.CreditsButton).assertExists()
        }

    }

    @Test
    fun expected_home_screen_with_auth_info() {

        // Arrange not needed because the default authInfo repo is already set to return a non null value

        ActivityScenario.launch(HomeActivity::class.java).use {
            testRule.onNodeWithTag(TestTags.Home.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Home.PlayButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.RankingsButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.CreditsButton).assertExists()
            testRule.onNodeWithTag(TestTags.Home.LogoutButton).assertExists()
        }

    }

    @Test
    fun pressing_sign_in_button_navigates_to_login_activity(){

        // Arrange
        application.authInfoRepository = mockRepo

        ActivityScenario.launch(HomeActivity::class.java).use{

            testRule.onNodeWithTag(TestTags.Home.SignInButton).performClick()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Auth.Screen).assertExists()
        }

    }

    @Test
    fun pressing_credits_button_navigates_to_info_screen_activity_with_no_auth(){

        // Arrange
        application.authInfoRepository = mockRepo

        ActivityScenario.launch(HomeActivity::class.java).use{

            testRule.onNodeWithTag(TestTags.Home.CreditsButton).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Info.Screen).assertExists()

        }

    }

    @Test
    fun pressing_credits_button_navigates_to_info_screen_activity_with_auth(){


        ActivityScenario.launch(HomeActivity::class.java).use{

            testRule.onNodeWithTag(TestTags.Home.CreditsButton).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Info.Screen).assertExists()
        }

    }

    @Test
    fun pressing_rankings_button_navigates_to_rankings_activity_with_no_auth(){

        // Arrange
        application.authInfoRepository = mockRepo

        ActivityScenario.launch(HomeActivity::class.java).use{

            testRule.onNodeWithTag(TestTags.Home.RankingsButton).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Rankings.Screen).assertExists()

        }

    }

    @Test
    fun pressing_rankings_button_navigates_to_rankings_activity_with_auth(){

        // Arrange not needed because mockk already returns an auth info not null

        ActivityScenario.launch(HomeActivity::class.java).use{

            testRule.onNodeWithTag(TestTags.Home.RankingsButton).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Home.Screen).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Rankings.Screen).assertExists()

        }

    }

}