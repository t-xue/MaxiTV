package com.iptv.player.data.parser

import com.iptv.player.data.local.entity.ChannelEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * M3U 播放列表解析器
 *
 * 支持标准 M3U/M3U8 格式和 IPTV 扩展属性：
 * - tvg-id: EPG 频道标识
 * - tvg-name: 频道名称 (用于 EPG 匹配)
 * - tvg-logo: 频道 Logo URL
 * - group-title: 频道分组
 * - tvg-shift: EPG 时间偏移
 */
@Singleton
class M3uParser @Inject constructor() {

    /**
     * 解析 M3U 内容
     * @param content M3U 文件内容
     * @param playlistId 所属播放列表 ID
     * @return 解析后的频道列表
     */
    fun parse(content: String, playlistId: Long): List<ChannelEntity> {
        val channels = mutableListOf<ChannelEntity>()
        val lines = content.lines()
        var currentExtInf: ExtInfInfo? = null
        var order = 0

        for (line in lines) {
            val trimmedLine = line.trim()

            when {
                // 跳过 M3U 头部
                trimmedLine.startsWith("#EXTM3U") -> {
                    // 可以解析头部属性，如 x-tvg-url
                    continue
                }

                // 解析扩展信息行
                trimmedLine.startsWith("#EXTINF:") -> {
                    currentExtInf = parseExtInf(trimmedLine)
                }

                // 跳过其他注释
                trimmedLine.startsWith("#") -> {
                    continue
                }

                // 这是流 URL
                trimmedLine.isNotBlank() -> {
                    currentExtInf?.let { info ->
                        channels.add(
                            ChannelEntity(
                                name = info.name.ifBlank { "Unknown Channel" },
                                url = trimmedLine,
                                logo = info.tvgLogo.ifBlank { null },
                                group = info.groupTitle.ifBlank { null },
                                tvgId = info.tvgId.ifBlank { null },
                                tvgName = info.tvgName.ifBlank { null },
                                playlistId = playlistId,
                                sortOrder = order++
                            )
                        )
                    }
                    currentExtInf = null
                }
            }
        }

        return channels
    }

    /**
     * 解析 #EXTINF 行
     */
    private fun parseExtInf(line: String): ExtInfInfo {
        // 提取属性
        val tvgId = extractAttribute(line, "tvg-id")
        val tvgName = extractAttribute(line, "tvg-name")
        val tvgLogo = extractAttribute(line, "tvg-logo")
        val groupTitle = extractAttribute(line, "group-title")

        // 提取频道名称 (逗号后面的文本)
        val name = line.substringAfterLast(",").trim()

        return ExtInfInfo(
            tvgId = tvgId,
            tvgName = tvgName,
            tvgLogo = tvgLogo,
            groupTitle = groupTitle,
            name = name
        )
    }

    /**
     * 从行中提取属性值
     * @param line 完整的 EXTINF 行
     * @param attributeName 属性名称
     * @return 属性值，如果不存在返回空字符串
     */
    private fun extractAttribute(line: String, attributeName: String): String {
        val regex = """$attributeName="([^"]*)"""".toRegex()
        return regex.find(line)?.groupValues?.get(1) ?: ""
    }

    /**
     * EXTINF 信息数据类
     */
    private data class ExtInfInfo(
        val tvgId: String,
        val tvgName: String,
        val tvgLogo: String,
        val groupTitle: String,
        val name: String
    )
}
