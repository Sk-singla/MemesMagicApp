package com.samarth.memes_magic.utils

import com.plcoding.doodlekong.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestCoroutineDispatcherProviderAndroid: DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val io: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val default: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
}