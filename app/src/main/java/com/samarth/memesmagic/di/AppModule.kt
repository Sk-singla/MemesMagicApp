package com.samarth.memesmagic.di

import android.content.Context
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.data.remote.ImageFlipApi
import com.samarth.memesmagic.data.remote.MemeApi
import com.samarth.memesmagic.data.remote.MemeGithubApi
import com.samarth.memesmagic.data.remote.MemeMakerApi
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.repository.MemeRepository
import com.samarth.memesmagic.util.Constants.BASE_URL
import com.samarth.memesmagic.util.Constants.IMAGE_FLIP_BASE_URL
import com.samarth.memesmagic.util.Constants.MEME_GITHUB_API_BASE_URL
import com.samarth.memesmagic.util.Constants.MEME_MAKER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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



}