package com.iptv.player.data.repository

import com.iptv.player.data.local.dao.PlaylistDao
import com.iptv.player.data.local.entity.PlaylistEntity
import com.iptv.player.data.parser.M3uParser
import com.iptv.player.data.remote.PlaylistApi
import com.iptv.player.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val channelRepository: ChannelRepository,
    private val playlistApi: PlaylistApi,
    private val m3uParser: M3uParser
) {
    /**
     * 获取所有播放列表
     */
    fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 从 URL 导入播放列表
     * @return 导入的频道数量
     */
    suspend fun importFromUrl(url: String, name: String? = null): Result<Int> {
        return try {
            // 1. 获取 M3U 内容
            val content = playlistApi.fetchPlaylist(url)

            // 2. 创建播放列表记录
            val playlist = PlaylistEntity(
                name = name ?: extractNameFromUrl(url),
                url = url
            )
            val playlistId = playlistDao.insert(playlist)

            // 3. 解析 M3U 内容
            val channels = m3uParser.parse(content, playlistId)

            // 4. 保存频道到数据库
            channelRepository.insertChannels(channels)

            // 5. 更新播放列表的频道数量
            playlistDao.updateChannelCount(playlistId, channels.size)

            Result.success(channels.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 从本地内容导入播放列表
     * @return 导入的频道数量
     */
    suspend fun importFromContent(content: String, name: String): Result<Int> {
        return try {
            // 1. 创建播放列表记录
            val playlist = PlaylistEntity(name = name)
            val playlistId = playlistDao.insert(playlist)

            // 2. 解析 M3U 内容
            val channels = m3uParser.parse(content, playlistId)

            // 3. 保存频道到数据库
            channelRepository.insertChannels(channels)

            // 4. 更新播放列表的频道数量
            playlistDao.updateChannelCount(playlistId, channels.size)

            Result.success(channels.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 刷新播放列表 (重新从 URL 获取)
     */
    suspend fun refreshPlaylist(playlistId: Long): Result<Int> {
        return try {
            val playlist = playlistDao.getById(playlistId)
                ?: return Result.failure(Exception("Playlist not found"))

            if (playlist.url == null) {
                return Result.failure(Exception("Playlist has no URL"))
            }

            // 删除旧频道
            channelRepository.deleteChannelsByPlaylist(playlistId)

            // 重新导入
            val content = playlistApi.fetchPlaylist(playlist.url)
            val channels = m3uParser.parse(content, playlistId)
            channelRepository.insertChannels(channels)

            // 更新播放列表
            playlistDao.updateChannelCount(playlistId, channels.size)
            playlistDao.updateLastUpdated(playlistId)

            Result.success(channels.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 删除播放列表
     */
    suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.delete(PlaylistEntity(id = playlistId, name = "", channelCount = 0))
    }

    /**
     * 从 URL 提取名称
     */
    private fun extractNameFromUrl(url: String): String {
        return try {
            val path = url.substringAfter("://").substringAfter("/").substringBeforeLast(".")
            path.substringAfterLast("/").ifBlank { "Playlist" }
        } catch (e: Exception) {
            "Playlist"
        }
    }

    /**
     * Entity 转 Domain 模型
     */
    private fun PlaylistEntity.toDomain(): Playlist {
        return Playlist(
            id = id,
            name = name,
            url = url,
            filePath = filePath,
            channelCount = channelCount,
            lastUpdated = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(lastUpdated),
                java.time.ZoneId.systemDefault()
            ),
            isActive = isActive
        )
    }
}
