package com.iptv.player.data.repository

import com.iptv.player.data.local.dao.ChannelDao
import com.iptv.player.data.local.entity.ChannelEntity
import com.iptv.player.domain.model.Channel
import com.iptv.player.domain.model.EpgProgram
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelRepository @Inject constructor(
    private val channelDao: ChannelDao
) {
    /**
     * 获取所有频道
     */
    fun getAllChannels(): Flow<List<Channel>> {
        return channelDao.getAllChannels().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 按播放列表获取频道
     */
    fun getChannelsByPlaylist(playlistId: Long): Flow<List<Channel>> {
        return channelDao.getChannelsByPlaylist(playlistId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 获取所有分组
     */
    fun getAllGroups(): Flow<List<String>> {
        return channelDao.getAllGroups()
    }

    /**
     * 按分组获取频道
     */
    fun getChannelsByGroup(group: String): Flow<List<Channel>> {
        return channelDao.getChannelsByGroup(group).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 搜索频道
     */
    fun searchChannels(query: String): Flow<List<Channel>> {
        return channelDao.searchChannels(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 获取收藏频道
     */
    fun getFavoriteChannels(): Flow<List<Channel>> {
        return channelDao.getFavoriteChannels().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 根据 ID 获取频道
     */
    suspend fun getChannelById(id: Long): Channel? {
        return channelDao.getById(id)?.toDomain()
    }

    /**
     * 切换收藏状态
     */
    suspend fun toggleFavorite(channelId: Long) {
        val isFavorite = channelDao.isFavorite(channelId)
        channelDao.updateFavoriteStatus(channelId, !isFavorite)
    }

    /**
     * 设置收藏状态
     */
    suspend fun setFavorite(channelId: Long, isFavorite: Boolean) {
        channelDao.updateFavoriteStatus(channelId, isFavorite)
    }

    /**
     * 批量插入频道
     */
    suspend fun insertChannels(channels: List<ChannelEntity>) {
        channelDao.insertAll(channels)
    }

    /**
     * 按播放列表删除频道
     */
    suspend fun deleteChannelsByPlaylist(playlistId: Long) {
        channelDao.deleteByPlaylistId(playlistId)
    }

    /**
     * Entity 转 Domain 模型
     */
    private fun ChannelEntity.toDomain(): Channel {
        return Channel(
            id = id,
            name = name,
            url = url,
            logo = logo,
            group = group,
            tvgId = tvgId,
            tvgName = tvgName,
            isFavorite = isFavorite,
            playlistId = playlistId,
            userAgent = userAgent,
            httpReferer = httpReferer
        )
    }
}
