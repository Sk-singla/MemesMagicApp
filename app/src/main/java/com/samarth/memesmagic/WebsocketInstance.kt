package com.samarth.memesmagic

import android.app.Application
import com.google.gson.Gson
import com.samarth.memesmagic.data.remote.ws.FlowStreamAdapter
import com.samarth.memesmagic.data.remote.WebSocketApi
import com.samarth.memesmagic.data.remote.ws.CustomGsonMessageAdapter
import com.samarth.memesmagic.util.Constants
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class WebsocketInstance(val token: String,val app:Application,val gson:Gson) {


    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        )
        .addInterceptor {
            val request = it.request()
            val newRequest = request.newBuilder()
                .addHeader("Authorization",token)
                .build()
            it.proceed(newRequest)
        }
        .build()

    val a = Scarlet.Builder()
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