package com.iptv.player.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iptv.player.data.local.entity.CustomChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: CustomChannelEntity): Long

    @Query("SELECT * FROM custom_channels ORDER BY createdAt DESC")
    fun getAllCustomChannels(): Flow<List<CustomChannelEntity>>

    @Query("SELECT * FROM custom_channels WHERE id = :id")
    suspend fun getById(id: Long): CustomChannelEntity?

    @Delete
    suspend fun delete(channel: CustomChannelEntity)
}
