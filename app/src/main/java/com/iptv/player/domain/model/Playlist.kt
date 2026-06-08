package com.iptv.player.domain.model

import java.time.LocalDateTime

/**
 * 播放列表领域模型
 */
data class Playlist(
    val id: Long = 0,
    val name: String,
    val url: String? = null,
    val filePath: String? = null,
    val channelCount: Int = 0,
    val lastUpdated: LocalDateTime? = null,
    val isActive: Boolean = true
)
