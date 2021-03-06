package com.samarth.memesmagic.ui.screens.home.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.UserSearchItem
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    parentNavController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val searchKeyword by remember {
        searchViewModel.searchKeyWord
    }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier =Modifier.fillMaxSize()
    ) {

        CustomTextField(
            value = searchKeyword,
            onValueChange =  {
                searchViewModel.searchKeyWord.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    searchViewModel.searchUser()
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        searchViewModel.searchUser()
                    },
                    enabled = !searchViewModel.isLoading.value
                ){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            },
            placeholder = "Search People",
            maxLines = 1
        )



        when {
            searchViewModel.isLoading.value -> {

                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

                    CircularProgressIndicator()

                }
            }
            searchViewModel.loadError.value.isNotEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
                    Text(
                        text = "No User Found!",
                        style = MaterialTheme.typography.h6,
                        modifier =Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Top
                ) {

                    items(searchViewModel.usersList.value) { userInfo ->

                        UserSearchItem(
                            userInfo = userInfo,
                            following = searchViewModel.isFollowingToUser(userInfo),
                            onFollowUnFollowBtnPressed = { onSuccess ->
                                    searchViewModel.followUnfollowToggle(userInfo, onSuccess)
//                                onSuccess()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    parentNavController.navigate("$ANOTHER_USER_PROFILE_SCREEN/${userInfo.email}")
                                }
                                .padding(vertical = 8.dp)
                        )

                    }

                    item {
                        Spacer(modifier = Modifier.padding(32.dp))
                    }

                }

            }
        }

    }
}