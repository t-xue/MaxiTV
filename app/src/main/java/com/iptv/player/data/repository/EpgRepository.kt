package com.iptv.player.data.repository

import com.iptv.player.data.local.dao.EpgDao
import com.iptv.player.data.local.entity.EpgProgramEntity
import com.iptv.player.data.parser.XmlTvParser
import com.iptv.player.data.remote.EpgApi
import com.iptv.player.domain.model.EpgProgram
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpgRepository @Inject constructor(
    private val epgDao: EpgDao,
    private val epgApi: EpgApi,
    private val xmlTvParser: XmlTvParser
) {
    /**
     * 获取频道当前正在播放的节目
     */
    suspend fun getCurrentProgram(channelId: String): EpgProgram? {
        return epgDao.getCurrentProgram(channelId)?.toDomain()
    }

    /**
     * 获取频道下一个节目
     */
    suspend fun getNextProgram(channelId: String): EpgProgram? {
        return epgDao.getNextProgram(channelId)?.toDomain()
    }

    /**
     * 获取指定日期的节目列表
     */
    fun getProgramsByDay(date: LocalDate): Flow<List<EpgProgram>> {
        val dayStart = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val dayEnd = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return epgDao.getProgramsByDay(dayStart, dayEnd).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 获取指定频道在时间范围内的节目
     */
    fun getProgramsByTimeRange(
        channelId: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Flow<List<EpgProgram>> {
        val start = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return epgDao.getProgramsByTimeRange(channelId, start, end).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 获取频道所有节目
     */
    fun getAllProgramsForChannel(channelId: String): Flow<List<EpgProgram>> {
        return epgDao.getAllProgramsForChannel(channelId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * 从 URL 同步 EPG 数据
     */
    suspend fun syncEpgFromUrl(url: String): Result<Int> {
        return try {
            // 1. 获取 XMLTV 内容
            val content = epgApi.fetchEpg(url)

            // 2. 解析 XMLTV
            val programs = xmlTvParser.parse(content)

            // 3. 保存到数据库
            epgDao.insertAll(programs)

            // 4. 清理过期数据 (7 天前)
            val weekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
            epgDao.deleteOldPrograms(weekAgo)

            Result.success(programs.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 清空所有 EPG 数据
     */
    suspend fun clearAllEpg() {
        epgDao.deleteAll()
    }

    /**
     * Entity 转 Domain 模型
     */
    private fun EpgProgramEntity.toDomain(): EpgProgram {
        return EpgProgram(
            id = id,
            channelId = channelId,
            title = title,
            description = description,
            startTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(startTime),
                ZoneId.systemDefault()
            ),
            endTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(endTime),
                ZoneId.systemDefault()
            ),
            category = category
        )
    }
}
