package com.iptv.player.domain.model

import java.time.LocalDateTime

/**
 * EPG 节目领域模型
 */
data class EpgProgram(
    val id: Long = 0,
    val channelId: String,  // tvg-id
    val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val category: String? = null
) {
    /**
     * 判断节目是否正在播放
     */
    fun isNowPlaying(): Boolean {
        val now = LocalDateTime.now()
        return now.isAfter(startTime) && now.isBefore(endTime)
    }

    /**
     * 获取节目进度百分比
     */
    fun getProgress(): Float {
        val now = LocalDateTime.now()
        if (now.isBefore(startTime)) return 0f
        if (now.isAfter(endTime)) return 1f

        val totalDuration = java.time.Duration.between(startTime, endTime).toMinutes()
        val elapsed = java.time.Duration.between(startTime, now).toMinutes()
        return if (totalDuration > 0) (elapsed.toFloat() / totalDuration).coerceIn(0f, 1f) else 0f
    }

    /**
     * 格式化时间范围
     */
    fun getTimeRange(): String {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
        return "${startTime.format(formatter)} - ${endTime.format(formatter)}"
    }
}
