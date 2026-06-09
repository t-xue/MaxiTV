package com.iptv.player.data.repository

import com.iptv.player.data.local.dao.CustomChannelDao
import com.iptv.player.data.local.entity.CustomChannelEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomChannelRepository @Inject constructor(
    private val customChannelDao: CustomChannelDao
) {
    /**
     * 添加自定义频道
     */
    suspend fun insertChannel(
        name: String,
        url: String,
        logo: String? = null,
        group: String? = null
    ): Long {
        val channel = CustomChannelEntity(
            name = name,
            url = url,
            logo = logo,
            group = group
        )
        return customChannelDao.insert(channel)
    }

    /**
     * 获取所有自定义频道
     */
    fun getAllChannels(): Flow<List<CustomChannelEntity>> {
        return customChannelDao.getAllCustomChannels()
    }

    /**
     * 根据 ID 获取自定义频道
     */
    suspend fun getChannelById(id: Long): CustomChannelEntity? {
        return customChannelDao.getById(id)
    }

    /**
     * 删除自定义频道
     */
    suspend fun deleteChannel(channel: CustomChannelEntity) {
        customChannelDao.delete(channel)
    }
}
