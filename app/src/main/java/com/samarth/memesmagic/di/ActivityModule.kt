package com.samarth.memesmagic.di

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.samarth.memesmagic.data.remote.ws.FlowStreamAdapter
import com.samarth.memesmagic.data.remote.WebSocketApi
import com.samarth.memesmagic.data.remote.ws.CustomGsonMessageAdapter
import com.samarth.memesmagic.util.Constants
import com.samarth.memesmagic.util.TokenHandler
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityModule {


    @ActivityRetainedScoped
    @Provides
    fun provideGson() = Gson()



    @ActivityRetainedScoped
    @Provides
    fun provideWebSocketMemeApi(
        app: Application,
        gson: Gson
    ): WebSocketApi {


        var token = "token"
        runBlocking {
            token = TokenHandler.getJwtToken(app.applicationContext) ?: "NULL"
        }

        Log.d("token",token)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
            .addInterceptor {
                val request = it.request()
                val newRequest = request.newBuilder()
                    .addHeader("Authorization","Bearer $token")
                    .build()
                it.proceed(newRequest)
            }
            .build()

        return Scarlet.Builder()
            .backoffStrategy(LinearBackoffStrategy(Constants.RECONNECT_INTERVAL))
            .lifecycle(
                AndroidLifecycle.ofApplicationForeground(app)
            )
            .webSocketFactory(
                okHttpClient.newWebSocketFactory(
                    Constants.BASE_URL_WS
                )
            )
            .addStreamAdapterFactory(FlowStreamAdapter.Factory)
            .addMessageAdapterFactory(
                CustomGsonMessageAdapter.Factory(gson)
            )
            .build()
            .create(WebSocketApi::class.java)
    }


}