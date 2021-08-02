package com.samarth.memesmagic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samarth.memesmagic.data.local.coverters.PrivateChatMessageConverters
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom

@Database(
    entities = [
        PrivateChatMessage::class,
        PrivateChatRoom::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(PrivateChatMessageConverters::class)
abstract class MemeDatabase : RoomDatabase(){
    abstract fun memeDao(): MemeDao
}