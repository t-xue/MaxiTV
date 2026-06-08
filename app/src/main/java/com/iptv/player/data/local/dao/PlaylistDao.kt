package com.iptv.player.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iptv.player.data.local.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity): Long

    @Update
    suspend fun update(playlist: PlaylistEntity)

    @Delete
    suspend fun delete(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getById(id: Long): PlaylistEntity?

    @Query("SELECT * FROM playlists ORDER BY lastUpdated DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE isActive = 1 ORDER BY lastUpdated DESC")
    fun getActivePlaylists(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlists SET channelCount = :count WHERE id = :playlistId")
    suspend fun updateChannelCount(playlistId: Long, count: Int)

    @Query("UPDATE playlists SET lastUpdated = :timestamp WHERE id = :playlistId")
    suspend fun updateLastUpdated(playlistId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM playlists")
    suspend fun deleteAll()
}
