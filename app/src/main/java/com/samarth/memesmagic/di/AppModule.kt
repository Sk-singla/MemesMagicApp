package com.samarth.memesmagic.di

import com.samarth.memesmagic.data.remote.MemeApi
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.repository.MemeRepository
import com.samarth.memesmagic.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideMemeRepository(
        api:MemeApi
    ):MemeRepo = MemeRepository(api)


    @Singleton
    @Provides
    fun provideMemeApi():MemeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MemeApi::class.java)
    }


}