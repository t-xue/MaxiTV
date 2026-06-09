package com.iptv.player.di

import android.content.Context
import androidx.room.Room
import com.iptv.player.data.local.MaxiTVDatabase
import com.iptv.player.data.local.dao.ChannelDao
import com.iptv.player.data.local.dao.CustomChannelDao
import com.iptv.player.data.local.dao.EpgDao
import com.iptv.player.data.local.dao.PlaylistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MaxiTVDatabase {
        return Room.databaseBuilder(
            context,
            MaxiTVDatabase::class.java,
            "maxitv_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideChannelDao(database: MaxiTVDatabase): ChannelDao {
        return database.channelDao()
    }

    @Provides
    fun providePlaylistDao(database: MaxiTVDatabase): PlaylistDao {
        return database.playlistDao()
    }

    @Provides
    fun provideEpgDao(database: MaxiTVDatabase): EpgDao {
        return database.epgDao()
    }

    @Provides
    fun provideCustomChannelDao(database: MaxiTVDatabase): CustomChannelDao {
        return database.customChannelDao()
    }
}
