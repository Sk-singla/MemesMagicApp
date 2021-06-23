package com.samarth.memesmagic.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.samarth.memesmagic.util.Constants.DATA_PREFERENCES_NAME_FOR_TOKEN
import com.samarth.memesmagic.util.Constants.JWT_TOKEN_KEY
import kotlinx.coroutines.flow.first

object TokenHandler {

    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = DATA_PREFERENCES_NAME_FOR_TOKEN)


    suspend fun saveJwtToken(context:Context,token:String){
        val dataStoreKey = stringPreferencesKey(JWT_TOKEN_KEY)
        context.dataStore.edit { tokens->
            tokens[dataStoreKey] = token
        }
    }

    suspend fun getJwtToken(context: Context):String?{
        val dataStoreKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey]
    }



















}