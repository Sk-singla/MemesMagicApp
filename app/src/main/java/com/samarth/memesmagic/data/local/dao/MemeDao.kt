package com.samarth.memesmagic.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface MemeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrivateMessage(message: PrivateChatMessage)


    @Query("SELECT * FROM PrivateChatMessage")
    fun getAllMessages():Flow<List<PrivateChatMessage>>

    fun getAllMessagesDistinct() =
        getAllMessages().distinctUntilChanged()



    @Query("SELECT * FROM PrivateChatMessage WHERE `to`= :email or `from`= :email")
    fun getAllMessagesFromUser(email: String):Flow<List<PrivateChatMessage>>




}