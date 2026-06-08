package com.iptv.player.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iptv.player.data.local.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    // 基础 CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: ChannelEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(channels: List<ChannelEntity>)

    @Update
    suspend fun update(channel: ChannelEntity)

    @Delete
    suspend fun delete(channel: ChannelEntity)

    @Query("DELETE FROM channels WHERE playlistId = :playlistId")
    suspend fun deleteByPlaylistId(playlistId: Long)

    // 查询
    @Query("SELECT * FROM channels WHERE id = :id")
    suspend fun getById(id: Long): ChannelEntity?

    @Query("SELECT * FROM channels WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<ChannelEntity?>

    @Query("SELECT * FROM channels ORDER BY sortOrder, name")
    fun getAllChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE playlistId = :playlistId ORDER BY sortOrder, name")
    fun getChannelsByPlaylist(playlistId: Long): Flow<List<ChannelEntity>>

    // 分组查询
    @Query("SELECT DISTINCT `group` FROM channels WHERE `group` IS NOT NULL ORDER BY `group`")
    fun getAllGroups(): Flow<List<String>>

    @Query("SELECT * FROM channels WHERE `group` = :group ORDER BY name")
    fun getChannelsByGroup(group: String): Flow<List<ChannelEntity>>

    // 搜索
    @Query("SELECT * FROM channels WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun searchChannels(query: String): Flow<List<ChannelEntity>>

    // 收藏
    @Query("SELECT * FROM channels WHERE isFavorite = 1 ORDER BY name")
    fun getFavoriteChannels(): Flow<List<ChannelEntity>>

    @Query("UPDATE channels SET isFavorite = :isFavorite WHERE id = :channelId")
    suspend fun updateFavoriteStatus(channelId: Long, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM channels WHERE id = :channelId")
    suspend fun isFavorite(channelId: Long): Boolean

    // EPG 关联查询
    @Query("SELECT * FROM channels WHERE tvgId = :tvgId")
    suspend fun getChannelByTvgId(tvgId: String): ChannelEntity?

    // 统计
    @Query("SELECT COUNT(*) FROM channels")
    suspend fun getChannelCount(): Int

    @Query("SELECT COUNT(*) FROM channels WHERE playlistId = :playlistId")
    suspend fun getChannelCountByPlaylist(playlistId: Long): Int
}
