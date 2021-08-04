package com.samarth.memesmagic.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.database.MemeDatabase
import com.samarth.memesmagic.data.remote.*
import com.samarth.memesmagic.data.remote.ws.CustomGsonMessageAdapter
import com.samarth.memesmagic.data.remote.ws.FlowStreamAdapter
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.repository.MemeRepository
import com.samarth.memesmagic.util.Constants
import com.samarth.memesmagic.util.Constants.BASE_URL
import com.samarth.memesmagic.util.Constants.IMAGE_FLIP_BASE_URL
import com.samarth.memesmagic.util.Constants.MEME_GITHUB_API_BASE_URL
import com.samarth.memesmagic.util.Constants.MEME_MAKER_BASE_URL
import com.samarth.memesmagic.util.TokenHandler
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideMemeRepository(
        api:MemeApi,
        imageFlipApi: ImageFlipApi,
        memeMakerApi: MemeMakerApi,
        memeGithubApi: MemeGithubApi,
        @ApplicationContext context: Context
    ):MemeRepo = MemeRepository(api,imageFlipApi,memeMakerApi,memeGithubApi,context)


    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }


    @Singleton
    @Provides
    fun provideOkHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideMemeApi(
        client:OkHttpClient
    ):MemeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MemeApi::class.java)
    }


    @Singleton
    @Provides
    fun provideImageFlipApi(
        client:OkHttpClient
    ):ImageFlipApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(IMAGE_FLIP_BASE_URL)
            .client(client)
            .build()
            .create(ImageFlipApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMemeMakerApi(
        client:OkHttpClient
    ):MemeMakerApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MEME_MAKER_BASE_URL)
            .client(client)
            .build()
            .create(MemeMakerApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMemeGithubApi(
        client:OkHttpClient
    ):MemeGithubApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MEME_GITHUB_API_BASE_URL)
            .client(client)
            .build()
            .create(MemeGithubApi::class.java)
    }


    @Singleton
    @Provides
    fun provideMemeDatabase(
        @ApplicationContext context: Context
    ): MemeDatabase {
        return Room.databaseBuilder(
            context,
            MemeDatabase::class.java,
            "Meme_db"
        ).build()
    }


    @Singleton
    @Provides
    fun provideMemeDao(
        memeDatabase: MemeDatabase
    ): MemeDao {
        return memeDatabase.memeDao()
    }





    @Singleton
    @Provides
    fun provideGson() = Gson()



    @Singleton
    @Provides
    fun provideWebSocketMemeApi(
        app: Application,
        gson: Gson
    ): WebSocketApi {


        var token = "NULL"

        CoroutineScope(Dispatchers.IO).launch {
            TokenHandler.getLiveJwtToken(app.applicationContext).collect {
                token = it
            }
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