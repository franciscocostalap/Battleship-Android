package com.example.battleshipmobile.login

import androidx.compose.ui.test.*
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.auth.AuthenticationActivity
import com.example.battleshipmobile.battleship.auth.AuthenticationFormType
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.utils.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AuthActivityEditingTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private val noAuthInfoRepo: AuthInfoService = mockk(relaxed=true){
        every { uid } returns null
    }

    @Test
    fun auth_form_state_survives_reconfiguration(){

        // Arrange
        application.authInfoService = noAuthInfoRepo

        val username = "testUsername"
        val password = "testPassword"

        ActivityScenario.launch(AuthenticationActivity::class.java).use{

            val fieldLabels = getFieldLabels(it)

            // Swap to register form
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).performTextInput(username)
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput(password)
            testRule.onNodeWithTag(TestTags.Other.PasswordVisibilityIcon).performClick()

            testRule.waitForIdle()

            it.recreate()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertTextEquals(username, fieldLabels.username)
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertTextEquals(password, fieldLabels.password)
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertContentIsVisible()
            testRule.onNodeWithTag(TestTags.Auth.Screen).assertFormType(AuthenticationFormType.Register)
        }

    }

    @Test
    fun all_fields_are_wiped_when_swaps_to_register_mode(){

        // Arrange
        application.authInfoService = noAuthInfoRepo

        ActivityScenario.launch(AuthenticationActivity::class.java).use {

            val fieldLabels = getFieldLabels(it)

            // Swap to register
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()

            testRule.waitForIdle()

            // Act
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).performTextInput("username")
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput("password")

            // Swap back to login
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()

            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertTextEquals("", fieldLabels.username)
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertTextEquals("", fieldLabels.password)
        }
    }

    @Test
    fun all_fields_are_wiped_when_swaps_back_to_login_mode(){

        // Arrange
        application.authInfoService = noAuthInfoRepo


        ActivityScenario.launch(AuthenticationActivity::class.java).use {

            val fieldLabels = getFieldLabels(it)

            // Swap to register mode
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()

            testRule.waitForIdle()

            // Act
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).performTextInput("username")
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput("password")

            // Swap back to login mode
            testRule.onNodeWithTag(TestTags.Auth.AuthSwapLink).performClick()


            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertTextEquals(fieldLabels.username, "")
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertTextEquals(fieldLabels.password, "")
        }
    }

    @Test
    fun clicking_password_visibility_icon_makes_password_visible(){

            // Arrange
            application.authInfoService = noAuthInfoRepo

            val password = "testPassword"

            ActivityScenario.launch(AuthenticationActivity::class.java).use {

                val fieldLabels = getFieldLabels(it)
                // Act
                testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput(password)
                testRule.onNodeWithTag(TestTags.Other.PasswordVisibilityIcon).performClick()

                testRule.waitForIdle()

                // Assert
                testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertTextEquals(password, fieldLabels.password)
                testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertContentIsVisible()
            }
    }

    @Test
    fun password_is_invisible_at_start(){

        // Arrange
        application.authInfoService = noAuthInfoRepo
        val invisiblePassword = "\u2022".repeat("password".length)

        ActivityScenario.launch(AuthenticationActivity::class.java).use {

            val fieldLabels = getFieldLabels(it)

            // Act
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput("password")

            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertContentIsNotVisible()
            testRule.onNodeWithTag(TestTags.Auth.PasswordField)
                .assertTextEquals(invisiblePassword, fieldLabels.password)
        }
    }

    @Test
    fun blank_password_input_puts_password_field_in_error_mode(){

            // Arrange
            application.authInfoService = noAuthInfoRepo

            ActivityScenario.launch(AuthenticationActivity::class.java).use {

                testRule.onNodeWithTag(TestTags.Auth.UsernameField).performTextInput("username")
                testRule.onNodeWithTag(TestTags.Auth.SubmitButton).performClick()

                testRule.waitForIdle()

                testRule.onNodeWithTag(TestTags.Auth.PasswordField).assertIsError()

                // Assert
                testRule.onNodeWithTag(TestTags.Auth.PasswordField)
                    .onSiblings()
                    .filter(hasTestTag(TestTags.Other.FieldErrorText))
                    .assertCountEquals(1)
            }

    }

    @Test
    fun blank_username_input_puts_password_field_in_error_mode(){

        // Arrange
        application.authInfoService = noAuthInfoRepo

        ActivityScenario.launch(AuthenticationActivity::class.java).use {

            testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput("password2!")
            testRule.onNodeWithTag(TestTags.Auth.SubmitButton).performClick()

            testRule.waitForIdle()

            testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertIsError()

            // Assert Error Text Exists
            testRule.onNodeWithTag(TestTags.Auth.UsernameField)
                .onSiblings()
                .filter(hasTestTag(TestTags.Other.FieldErrorText))
                .assertCountEquals(1)
        }

    }

    @Test
    fun error_mode_for_a_field_is_cleared_as_soon_as_any_input_is_entered(){

            // Arrange
            application.authInfoService = noAuthInfoRepo

            ActivityScenario.launch(AuthenticationActivity::class.java).use {

                testRule.onNodeWithTag(TestTags.Auth.PasswordField).performTextInput("password2!")
                testRule.onNodeWithTag(TestTags.Auth.SubmitButton).performClick()

                testRule.waitForIdle()

                testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertIsError()

                // Act
                testRule.onNodeWithTag(TestTags.Auth.UsernameField).performTextInput("u")

                testRule.waitForIdle()

                // Assert
                testRule.onNodeWithTag(TestTags.Auth.UsernameField).assertIsNotError()

                // Assert Error Text Does Not Exist
                testRule.onNodeWithTag(TestTags.Auth.UsernameField)
                    .onSiblings()
                    .filter(hasTestTag(TestTags.Other.FieldErrorText))
                    .assertCountEquals(0)
            }

    }

    data class AuthFieldLabels(
        val username: String,
        val password: String
    )

    data class ErrorLabels(
        val username: String="",
        val password: String=""
    )

    /**
     * Gets the labels from the the [AuthenticationActivity]
     */
    private fun getFieldLabels(
        activityScenario: ActivityScenario<AuthenticationActivity>
    ): AuthFieldLabels{
        return activityScenario.getFromActivity {
            AuthFieldLabels(
                username = getString(R.string.username_field_label),
                password = getString(R.string.password_field_label)
            )
        }
    }


}