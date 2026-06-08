package com.iptv.player.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * 播放列表网络 API
 * 用于从 URL 获取 M3U 文件内容
 */
interface PlaylistApi {

    /**
     * 获取 M3U 文件内容
     * @param url M3U 文件的完整 URL
     * @return M3U 文件内容 (纯文本)
     */
    @GET
    suspend fun fetchPlaylist(@Url url: String): String
}
