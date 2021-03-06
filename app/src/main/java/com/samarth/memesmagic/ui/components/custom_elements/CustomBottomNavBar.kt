package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.samarth.memesmagic.ui.screens.home.HomeSections
import com.samarth.memesmagic.util.Screens.HOME_CREATE
import com.samarth.memesmagic.util.Screens.HOME_NOTIFICATIONS

@ExperimentalMaterialApi
@Composable
fun CustomBottomNavBar(
    navController: NavHostController,
    parentNavController:NavHostController,
    tabs: Array<HomeSections>,
    notificationCount: Int
) {
    val currentRoute = currentRoute(navController = navController)
    val sections = remember {
        HomeSections.values()
    }
    val routes = remember {
        sections.map { it.route }
    }

    if(currentRoute in routes) {

        val currentSection = sections.first{ it.route == currentRoute}
        CustomBottomNavigation(
            backgroundColor = MaterialTheme.colors.surface
        ){


            tabs.forEach { section ->

                val isSelected = section == currentSection
                CustomBottomNavigationItem(
                    isSelected = isSelected,
                    section = section,
                    currentRoute = currentRoute,
                    parentNavController = parentNavController,
                    navController = navController,
                    notificationCount = notificationCount
                )

            }

        }

    }
}


@Composable
fun currentRoute(navController: NavHostController):String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}


@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
    content: @Composable RowScope.() -> Unit
) {


    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(BottomNavigationHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content,
            verticalAlignment = Alignment.CenterVertically
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun RowScope.CustomBottomNavigationItem(
    isSelected:Boolean,
    section:HomeSections,
    currentRoute:String?,
    parentNavController: NavHostController,
    navController: NavHostController,
    notificationCount: Int = 0
) {

    if(section.route == HOME_CREATE){
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = section.focusedIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(0.dp),
                    tint = Color.Unspecified
                )
            },
            selected = isSelected,
            onClick = {
                if (currentRoute != section.route) {
                    parentNavController.navigate(section.route)
//                    val template = Gson().toJson(MemeTemplate("Hi","abcd"))
//                    template?.let { memeTemp ->
//                        parentNavController.navigate("${Screens.EDIT_SCREEN}/$memeTemp")
//                    }
                }
            },
        )


    } else {

        BottomNavigationItem(
            icon = {

                if(section.route == HOME_NOTIFICATIONS && notificationCount > 0){

                    BadgeBox(
                        backgroundColor = MaterialTheme.colors.primary,
                        badgeContent = {
                            Text(text = "$notificationCount")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = if (isSelected) section.focusedIcon else section.nonFocusIcon),
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(id = if (isSelected) section.focusedIcon else section.nonFocusIcon),
                        contentDescription = null,
                        modifier = Modifier.padding(0.dp)
                    )
                }



            },
            selected = isSelected,
            onClick = {
                if (currentRoute != section.route) {

                    navController.navigate(section.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }

                }
            },
            selectedContentColor = MaterialTheme.colors.onPrimary,
            unselectedContentColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.7f)
        )
    }

}
private val BottomNavigationHeight = 56.dp




