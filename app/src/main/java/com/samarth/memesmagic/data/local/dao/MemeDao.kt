package com.samarth.memesmagic.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.samarth.memesmagic.data.local.entities.relations.PrivateChatRoomWithPrivateChatMessages
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface MemeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrivateMessage(message: PrivateChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrivateChatRoom(room:PrivateChatRoom)


    @Query("UPDATE PrivateChatMessage SET msgStatus = :status WHERE id= :msgId")
    suspend fun updatePrivateChatMessageStatus(msgId:String,status:String)

    @Query("SELECT * FROM PrivateChatMessage")
    fun getAllMessages():Flow<List<PrivateChatMessage>>

    @Transaction
    @Query("SELECT * FROM PrivateChatRoom")
    fun getAllPrivateChatRooms():Flow<List<PrivateChatRoomWithPrivateChatMessages>>

    @Query("SELECT * FROM PrivateChatRoom WHERE userEmail = :email")
    fun getAllMessagesFromUser(email: String):Flow<PrivateChatRoomWithPrivateChatMessages>

    @Query("SELECT * FROM PrivateChatMessage WHERE msgStatus = :status")
    fun getAllMessagesWhereStatus(status:String):List<PrivateChatMessage>

    @Query("DELETE FROM PrivateChatMessage")
    suspend fun clearData()

}