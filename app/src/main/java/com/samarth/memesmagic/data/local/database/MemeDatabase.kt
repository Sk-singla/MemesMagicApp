package com.samarth.memesmagic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samarth.memesmagic.data.local.coverters.PrivateChatMessageConverters
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage

@Database(entities = [PrivateChatMessage::class],version = 1,exportSchema = false)
@TypeConverters(PrivateChatMessageConverters::class)
abstract class MemeDatabase : RoomDatabase(){
    abstract fun memeDao(): MemeDao
}