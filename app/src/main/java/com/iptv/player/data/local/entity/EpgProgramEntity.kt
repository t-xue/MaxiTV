package com.iptv.player.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * EPG 节目数据库实体
 */
@Entity(
    tableName = "epg_programs",
    indices = [
        Index(value = ["channelId"]),
        Index(value = ["startTime"]),
        Index(value = ["endTime"])
    ]
)
data class EpgProgramEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val channelId: String,  // 对应 tvg-id
    val title: String,
    val description: String? = null,
    val startTime: Long,    // Unix 时间戳
    val endTime: Long,      // Unix 时间戳
    val category: String? = null
)
