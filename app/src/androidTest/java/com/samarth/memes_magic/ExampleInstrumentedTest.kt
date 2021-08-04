package com.samarth.memes_magic

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.samarth.memesmagic.ui.MainActivity
import com.samarth.memesmagic.ui.screens.landing_page.SplashScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalFoundationApi
@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun splash_screen_logo_exists() {
        composeTestRule.setContent {
            SplashScreen(
                navController = rememberNavController(),
                testing = true
            )
        }

        composeTestRule.onNodeWithContentDescription(
            "Logo"
        ).assertExists()
    }

}