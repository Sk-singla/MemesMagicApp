package com.samarth.memesmagic.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface MemeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrivateMessage(message: PrivateChatMessage)


    @Query("UPDATE PrivateChatMessage SET msgStatus = :status WHERE id= :msgId")
    suspend fun updatePrivateChatMessageStatus(msgId:String,status:String)

    @Query("SELECT * FROM PrivateChatMessage")
    fun getAllMessages():Flow<List<PrivateChatMessage>>

    @Query("SELECT * FROM PrivateChatMessage WHERE `to`= :email or `from`= :email")
    fun getAllMessagesFromUser(email: String):Flow<List<PrivateChatMessage>>

    @Query("SELECT * FROM PrivateChatMessage WHERE msgStatus = :status")
    fun getAllMessagesWhereStatus(status:String):List<PrivateChatMessage>

    @Query("DELETE FROM PrivateChatMessage")
    fun clearData()

}