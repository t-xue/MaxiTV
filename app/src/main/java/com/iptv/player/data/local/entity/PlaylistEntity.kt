package com.iptv.player.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 播放列表数据库实体
 */
@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val url: String? = null,
    val filePath: String? = null,
    val channelCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
