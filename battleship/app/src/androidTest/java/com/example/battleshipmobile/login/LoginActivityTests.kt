package com.example.battleshipmobile.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.login.LoginActivity
import com.example.battleshipmobile.ui.TestTags
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTests {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.battleshipmobile", appContext.packageName)
    }

    @get:Rule
    val loginActivityRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun loginScreenIsDisplayed() {
        loginActivityRule.onNodeWithTag(TestTags.Login.Screen).assertExists()
        loginActivityRule.onNodeWithTag(TestTags.Login.UsernameField).assertExists()
        loginActivityRule.onNodeWithTag(TestTags.Login.PasswordField).assertExists()
        loginActivityRule.onNodeWithTag(TestTags.Login.SubmitButton).assertExists()
    }

    @Test
    fun fieldContentSurvivesReconfiguration(){
        val username = "testUsername"
        val password = "testPassword"
        val invisiblePassword = "•".repeat(password.length)
        val usernameLabel = loginActivityRule.activity.getString(R.string.username_field_label)
        val passwordLabel = loginActivityRule.activity.getString(R.string.password_field_label)
        val usernameFieldTag = TestTags.Login.UsernameField
        val passwordFieldTag = TestTags.Login.PasswordField
        val passwordVisibilityToggleTag = TestTags.Other.PasswordVisibilityIcon

        loginActivityRule.onNodeWithTag(usernameFieldTag).performTextInput(username)
        loginActivityRule.onNodeWithTag(passwordFieldTag).performTextInput(password)
        loginActivityRule.onNodeWithTag(passwordFieldTag).assertTextEquals(invisiblePassword, passwordLabel)

        loginActivityRule.activityRule.scenario.recreate()

        loginActivityRule.onNodeWithTag(usernameFieldTag).assertTextEquals(username, usernameLabel)

        loginActivityRule.onNodeWithTag(passwordFieldTag).assertTextEquals(invisiblePassword, passwordLabel)
        loginActivityRule.onNodeWithTag(passwordVisibilityToggleTag).performClick()
        loginActivityRule.onNodeWithTag(passwordFieldTag).assertTextEquals(password, passwordLabel)
    }

}