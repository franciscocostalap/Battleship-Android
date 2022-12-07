package com.example.battleshipmobile.layout_definition

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battleshipmobile.battleship.layout_definition.LayoutDefinitionActivity
import com.example.battleshipmobile.ui.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LayoutDefinitionActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<LayoutDefinitionActivity>()


    @Test
    fun layout_exists() {
        testRule.onNodeWithTag(TestTags.LayoutDefinition.Screen).assertExists()
        testRule.onNodeWithTag(TestTags.LayoutDefinition.Board).assertExists()
        testRule.onNodeWithTag(TestTags.LayoutDefinition.FleetComposition).assertExists()
    }

}