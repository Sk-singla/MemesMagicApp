package com.samarth.memesmagic.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.samarth.memesmagic.util.Constants.DATA_PREFERENCES_NAME_FOR_TOKEN
import com.samarth.memesmagic.util.Constants.EMAIL_KEY
import com.samarth.memesmagic.util.Constants.JWT_TOKEN_KEY
import com.samarth.memesmagic.util.Constants.REWARD_ID_KEY
import kotlinx.coroutines.flow.first

object TokenHandler {

    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = DATA_PREFERENCES_NAME_FOR_TOKEN)


    suspend fun saveJwtToken(context:Context,token:String,email:String){
        val jwtTokeKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val emailKey = stringPreferencesKey(EMAIL_KEY)
        context.dataStore.edit { tokens->
            tokens[jwtTokeKey] = token
            tokens[emailKey] = email
        }
    }

    suspend fun getJwtToken(context: Context):String?{
        val dataStoreKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun getEmail(context: Context):String? {
        val emailKey = stringPreferencesKey(EMAIL_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[emailKey]
    }


    suspend fun saveRewardId(context: Context,rewardId:String){
        val rewardKey = stringPreferencesKey(REWARD_ID_KEY)
        context.dataStore.edit { tokens->
            tokens[rewardKey] = rewardId
        }
    }

    suspend fun getRewardId(context: Context):String?{
        val rewardKey = stringPreferencesKey(REWARD_ID_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[rewardKey]
    }



















}