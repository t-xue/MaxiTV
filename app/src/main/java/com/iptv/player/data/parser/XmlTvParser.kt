package com.iptv.player.data.parser

import com.iptv.player.data.local.entity.EpgProgramEntity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * XMLTV EPG 数据解析器
 *
 * XMLTV 格式示例:
 * <tv>
 *   <programme start="20240101120000 +0800" stop="20240101130000 +0800" channel="CNN.us">
 *     <title lang="en">News Hour</title>
 *     <desc lang="en">Latest news coverage</desc>
 *     <category lang="en">News</category>
 *   </programme>
 * </tv>
 */
@Singleton
class XmlTvParser @Inject constructor() {

    companion object {
        // XMLTV 时间格式: yyyyMMddHHmmss +HHmm
        private val XMLTV_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss Z")
        private val XMLTV_DATE_FORMAT_NO_TZ = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    }

    /**
     * 解析 XMLTV 内容
     * @param content XMLTV XML 内容
     * @return 解析后的 EPG 节目列表
     */
    fun parse(content: String): List<EpgProgramEntity> {
        val programs = mutableListOf<EpgProgramEntity>()

        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(content))

            var eventType = parser.eventType
            var currentChannelId: String? = null
            var currentStart: Long = 0
            var currentStop: Long = 0
            var currentTitle: String = ""
            var currentDescription: String? = null
            var currentCategory: String? = null
            var inProgramme = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "programme" -> {
                                inProgramme = true
                                currentChannelId = parser.getAttributeValue(null, "channel")
                                currentStart = parseXmlTvTime(
                                    parser.getAttributeValue(null, "start")
                                )
                                currentStop = parseXmlTvTime(
                                    parser.getAttributeValue(null, "stop")
                                )
                                currentTitle = ""
                                currentDescription = null
                                currentCategory = null
                            }
                            "title" -> {
                                if (inProgramme) {
                                    currentTitle = readText(parser)
                                }
                            }
                            "desc" -> {
                                if (inProgramme) {
                                    currentDescription = readText(parser)
                                }
                            }
                            "category" -> {
                                if (inProgramme) {
                                    currentCategory = readText(parser)
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "programme" && inProgramme) {
                            if (currentChannelId != null && currentTitle.isNotBlank()) {
                                programs.add(
                                    EpgProgramEntity(
                                        channelId = currentChannelId,
                                        title = currentTitle,
                                        description = currentDescription,
                                        startTime = currentStart,
                                        endTime = currentStop,
                                        category = currentCategory
                                    )
                                )
                            }
                            inProgramme = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return programs
    }

    /**
     * 读取 XML 元素的文本内容
     */
    private fun readText(parser: XmlPullParser): String {
        val result = StringBuilder()
        if (parser.next() == XmlPullParser.TEXT) {
            result.append(parser.text)
            parser.nextTag()
        }
        return result.toString().trim()
    }

    /**
     * 解析 XMLTV 时间格式
     * 支持格式: "20240101120000 +0800" 或 "20240101120000"
     * @return Unix 时间戳 (毫秒)
     */
    private fun parseXmlTvTime(timeStr: String?): Long {
        if (timeStr.isNullOrBlank()) return 0

        return try {
            val trimmed = timeStr.trim()
            if (trimmed.contains(" ") || trimmed.contains("+") || trimmed.contains("-")) {
                // 带时区
                val zonedDateTime = ZonedDateTime.parse(trimmed, XMLTV_DATE_FORMAT)
                zonedDateTime.toInstant().toEpochMilli()
            } else {
                // 不带时区，使用系统默认时区
                val localDateTime = LocalDateTime.parse(trimmed, XMLTV_DATE_FORMAT_NO_TZ)
                localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            }
        } catch (e: Exception) {
            0L
        }
    }
}
