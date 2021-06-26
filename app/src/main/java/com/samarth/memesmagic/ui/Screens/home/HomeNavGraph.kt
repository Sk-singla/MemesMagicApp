package com.samarth.memesmagic.ui.Screens.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.Screens.home.create.CreateScreen
import com.samarth.memesmagic.ui.Screens.home.profile.ProfileScreen
import com.samarth.memesmagic.ui.Screens.home.rewards.RewardScreen
import com.samarth.memesmagic.ui.Screens.home.search.SearchScreen
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.HOME_CREATE
import com.samarth.memesmagic.util.Screens.HOME_FEED
import com.samarth.memesmagic.util.Screens.HOME_PROFILE
import com.samarth.memesmagic.util.Screens.HOME_REWARDS
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.HOME_SEARCH
import com.samarth.memesmagic.util.TokenHandler.getEmail


enum class HomeSections(
    val route:String,
    @StringRes val title:Int,
    @DrawableRes val nonFocusIcon:Int,
    @DrawableRes val focusedIcon:Int,
){
    FEED(HOME_FEED,R.string.home_feed,R.drawable.ic_outline_home,R.drawable.ic_filled_home),
    SEARCH(HOME_SEARCH,R.string.home_search,R.drawable.ic_outline_search,R.drawable.ic_filled_search),
    CREATE(HOME_CREATE,R.string.home_create,R.drawable.ic_create_24, R.drawable.ic_create_24),
    REWARDS(HOME_REWARDS,R.string.home_rewards,R.drawable.ic_reward,R.drawable.ic_filled_reward),
    PROFILE(HOME_PROFILE,R.string.home_profile,R.drawable.ic_outline_person,R.drawable.ic_person),

}


@ExperimentalFoundationApi
@Composable
fun HomeNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = HOME_FEED,modifier = modifier){

        composable(HOME_FEED){
            FeedScreen()
        }

        composable(HOME_SEARCH){
            SearchScreen()
        }

        composable(HOME_REWARDS){
            RewardScreen()
        }

        composable(HOME_PROFILE){
            ProfileScreen()
        }



    }































}


































