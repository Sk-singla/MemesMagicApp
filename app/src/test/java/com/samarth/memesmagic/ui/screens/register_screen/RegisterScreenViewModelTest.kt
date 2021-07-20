package com.samarth.memesmagic.ui.screens.register_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.samarth.memesmagic.repository.FAKE_EMAIL
import com.samarth.memesmagic.repository.FAKE_PASSWORD
import com.samarth.memesmagic.repository.FakeMemeRepository
import com.samarth.memesmagic.utils.TestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterScreenViewModelTest{

    private lateinit var registerScreenViewModel: RegisterScreenViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        registerScreenViewModel = RegisterScreenViewModel(
            FakeMemeRepository(),
            TestCoroutineDispatcherProvider()
        )
    }

    @Test
    fun `register with empty name, returns false`(){
        registerScreenViewModel.name.value = ""
        registerScreenViewModel.email.value = "abc@gmail.com"
        registerScreenViewModel.password.value = "asdfasdf"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value
        var result = true
        registerScreenViewModel.registerUser(
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
    fun `register with empty email, returns false`(){
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.email.value = ""
        registerScreenViewModel.password.value = "asdfasdf"
        var result = true
        registerScreenViewModel.registerUser(
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
    fun `register with empty password, returns false`(){
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.email.value = "asdf@gmail.com"
        registerScreenViewModel.password.value = ""
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value
        var result = true
        registerScreenViewModel.registerUser(
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
    fun `register with different password and confirmPassword, returns false`(){
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.email.value = "asdf@gmail.com"
        registerScreenViewModel.password.value = "ghijklmn"
        registerScreenViewModel.confirmPassword.value = "abcdefgh"
        var result = true
        registerScreenViewModel.registerUser(
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
    fun `register with unformatted email, returns false`(){
        registerScreenViewModel.email.value = "asdf"
        registerScreenViewModel.password.value = "asdfasdf"
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value

        var result = true
        registerScreenViewModel.registerUser(
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
    fun `register with small password, return false`(){
        registerScreenViewModel.email.value = "asdf@gmail.com"
        registerScreenViewModel.password.value = "abc"
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value

        var result = true

        registerScreenViewModel.registerUser(
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
    fun `register with large password, return false`(){
        registerScreenViewModel.email.value = "asdf@gmail.com"
        registerScreenViewModel.password.value = "abcasdfasdfasd"
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value

        var result = true

        registerScreenViewModel.registerUser(
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
    fun `register with already registered email, return false`(){
        registerScreenViewModel.email.value = FAKE_EMAIL
        registerScreenViewModel.password.value = FAKE_PASSWORD
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value


        var result = false
        registerScreenViewModel.registerUser(
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
    fun `register with valid information, return true`(){
        registerScreenViewModel.email.value = "abc@gmail.com"
        registerScreenViewModel.password.value = "thisIsPass"
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value


        var result = false
        registerScreenViewModel.registerUser(
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
    fun `while registration, isLoading value is true`(){

        registerScreenViewModel.email.value = "asdf@gmail.com"
        registerScreenViewModel.password.value = "asdfasdf"
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value


        var result = false
        registerScreenViewModel.registerUser(
            onSuccess = {
                result = registerScreenViewModel.isLoading.value
            },
            onFail = {
                result = registerScreenViewModel.isLoading.value
                print(it)
            }
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `After registration, isLoading value is false`(){

        registerScreenViewModel.email.value = "asdf@gmail.com"
        registerScreenViewModel.password.value = "asdfasdf"
        registerScreenViewModel.name.value = "name"
        registerScreenViewModel.confirmPassword.value = registerScreenViewModel.password.value


        registerScreenViewModel.registerUser(
            onSuccess = {},
            onFail = {
                print(it)
            }
        )
        assertThat(registerScreenViewModel.isLoading.value).isFalse()
    }

}