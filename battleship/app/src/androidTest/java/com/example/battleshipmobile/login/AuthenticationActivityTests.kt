package com.example.battleshipmobile.login

import androidx.compose.ui.test.*
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.auth.AuthenticationActivity
import com.example.battleshipmobile.battleship.auth.AuthenticationFormType
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.utils.PreserveDefaultDependencies
import com.example.battleshipmobile.utils.assertFormType
import com.example.battleshipmobile.utils.createPreserveDefaultDependenciesComposeRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AuthenticationActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private val mockRepo: AuthInfoService = mockk(relaxed = true) {
        every { uid } returns null
    }

    @Test
    fun auth_activity_starts_with_login_screen() {

        // Arrange
        application.authInfoService = mockRepo

        // Assert
        ActivityScenario.launch(AuthenticationActivity::class.java).use {
            testRule.onNodeWithTag(TestTags.Auth.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.RepeatPasswordField).assertDoesNotExist()
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.SubmitButton).assertExists()
        }
    }

    @Test
    fun login_screen_navigates_to_register_screen_when_auth_swap_link_is_clicked() {

        // Arrange
        application.authInfoService = mockRepo


        ActivityScenario.launch(AuthenticationActivity::class.java).use {

            // Act
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()

            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(TestTags.Auth.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.SubmitButton).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.Screen)
                .assertFormType(AuthenticationFormType.Register)
        }
    }

    @Test
    fun screen_navigates_to_login_screen_when_auth_swap_link_is_re_clicked() {

        // Arrange
        application.authInfoService = mockRepo


        ActivityScenario.launch(AuthenticationActivity::class.java).use {

            // Act
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()

            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(TestTags.Auth.Screen).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.SubmitButton).assertExists()
            testRule.onNodeWithTag(TestTags.Auth.Screen)
                .assertFormType(AuthenticationFormType.Login)
        }
    }

}