package com.samarth.memes_magic

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.samarth.memesmagic.ui.MainActivity
import com.samarth.memesmagic.ui.screens.landing_page.SplashScreen
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()



    lateinit var navController: NavController
    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun splash_screen_logo_exists() = testDispatcher.runBlockingTest{
        composeTestRule.setContent {
            navController = rememberNavController()
            SplashScreen(
                navController = navController,
                testing = true
            )
        }

        composeTestRule.onNodeWithContentDescription(
            "Logo"
        ).assertExists()
    }

}