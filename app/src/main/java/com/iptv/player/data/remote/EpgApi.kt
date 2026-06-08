package com.iptv.player.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * EPG 数据网络 API
 * 用于从 URL 获取 XMLTV 格式的 EPG 数据
 */
interface EpgApi {

    /**
     * 获取 XMLTV EPG 数据
     * @param url XMLTV 文件的完整 URL
     * @return XMLTV XML 内容
     */
    @GET
    suspend fun fetchEpg(@Url url: String): String
}
