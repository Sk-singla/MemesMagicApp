package com.samarth.memesmagic.ui.screens.login_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.samarth.memesmagic.repository.FAKE_EMAIL
import com.samarth.memesmagic.repository.FAKE_PASSWORD
import com.samarth.memesmagic.repository.FakeMemeRepository
import com.samarth.memesmagic.utils.TestCoroutineDispatcherProvider
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginScreenViewModelTest {

    private lateinit var loginScreenViewModel: LoginScreenViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        loginScreenViewModel = LoginScreenViewModel(
            FakeMemeRepository(),
            TestCoroutineDispatcherProvider()
        )
    }

    @Test
    fun `login with empty email, returns false`(){
        loginScreenViewModel.email.value = ""
        loginScreenViewModel.password.value = "asdfasdf"
        var result = true
        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
            }
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `login with empty password, returns false`(){
        loginScreenViewModel.email.value = "asdf@gmail.com"
        loginScreenViewModel.password.value = ""
        var result = true
        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
            }
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `login with unformatted email, returns false`(){
        loginScreenViewModel.email.value = "asdf"
        loginScreenViewModel.password.value = "asdfasdf"
        var result = true
        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
            }
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `login with small password, return false`(){
        loginScreenViewModel.email.value = "asdf@gmail.com"
        loginScreenViewModel.password.value = "abc"
        var result = true

        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
                print(it)
            }
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `login with large password, return false`(){
        loginScreenViewModel.email.value = "asdf@gmail.com"
        loginScreenViewModel.password.value = "abcasdfasdfasd"
        var result = true

        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
                print(it)
            }
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `login with unregistered email and password, return false`(){
        loginScreenViewModel.email.value = "asdf@gmail.com"
        loginScreenViewModel.password.value = "passuwordu"

        var result = false
        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
                print(it)
            }
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `login with valid email and password, return true`(){
        loginScreenViewModel.email.value = FAKE_EMAIL
        loginScreenViewModel.password.value = FAKE_PASSWORD

        var result = false
        loginScreenViewModel.loginUser(
            onSuccess = {
                result = true
            },
            onFail = {
                result = false
                print(it)
            }
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `while login, isLoading value is true`(){

        loginScreenViewModel.email.value = FAKE_EMAIL
        loginScreenViewModel.password.value = FAKE_PASSWORD

        var result = false
        loginScreenViewModel.loginUser(
            onSuccess = {
                result = loginScreenViewModel.isLoading.value
            },
            onFail = {
                result = loginScreenViewModel.isLoading.value
                print(it)
            }
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `After login, isLoading value is false`(){

        loginScreenViewModel.email.value = FAKE_EMAIL
        loginScreenViewModel.password.value = FAKE_PASSWORD

        loginScreenViewModel.loginUser(
            onSuccess = {},
            onFail = {
                print(it)
            }
        )
        assertThat(loginScreenViewModel.isLoading.value).isFalse()
    }

}