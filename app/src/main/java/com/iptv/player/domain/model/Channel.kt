package com.iptv.player.domain.model

/**
 * 频道领域模型
 */
data class Channel(
    val id: Long = 0,
    val name: String,
    val url: String,
    val logo: String? = null,
    val group: String? = null,
    val tvgId: String? = null,
    val tvgName: String? = null,
    val isFavorite: Boolean = false,
    val playlistId: Long = 0,
    // EPG 相关
    val currentProgram: EpgProgram? = null,
    val nextProgram: EpgProgram? = null
)
