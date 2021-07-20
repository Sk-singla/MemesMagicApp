package com.samarth.memesmagic.ui.screens.another_user_profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.samarth.memesmagic.repository.FAKE_EMAIL
import com.samarth.memesmagic.repository.FakeMemeRepository
import com.samarth.memesmagic.utils.TestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AnotherUserProfileViewModelTest {

    private lateinit var anotherUserProfileViewModel: AnotherUserProfileViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup(){
        anotherUserProfileViewModel = AnotherUserProfileViewModel(
            FakeMemeRepository(),
            TestCoroutineDispatcherProvider()
        )
    }


    @Test
    fun callGetUserFunction_updateLocalUserVariable(){
        anotherUserProfileViewModel.getUser(
            "curuseremail@gmail.com",
            FAKE_EMAIL
        )
        Truth.assertThat(anotherUserProfileViewModel.user.value).isNotNull()
    }

    @Test
    fun followUser_updateIsFollowingVar(){
        anotherUserProfileViewModel.getUser(
            "curUser@gmail.com",
            FAKE_EMAIL
        )
        var result = false
        anotherUserProfileViewModel.followUnfollowToggle(
            "curUser@gmail.com",
            onSuccess = {
                result = anotherUserProfileViewModel.isFollowing.value
            }
        )
        Truth.assertThat(result).isTrue()
    }
}