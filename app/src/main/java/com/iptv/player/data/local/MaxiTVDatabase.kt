package com.iptv.player.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iptv.player.data.local.dao.ChannelDao
import com.iptv.player.data.local.dao.EpgDao
import com.iptv.player.data.local.dao.PlaylistDao
import com.iptv.player.data.local.entity.ChannelEntity
import com.iptv.player.data.local.entity.EpgProgramEntity
import com.iptv.player.data.local.entity.PlaylistEntity

@Database(
    entities = [
        ChannelEntity::class,
        PlaylistEntity::class,
        EpgProgramEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MaxiTVDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun epgDao(): EpgDao
}
