# MaxiTV v1.0.0 发布说明

**发布日期**: 2026-06-08

🎉 **MaxiTV 1.0.0 正式发布！** 这是首个正式版本，一个简洁、现代的 Android IPTV 播放器。

---

## ✨ 核心功能

### 📺 播放列表管理
- 支持从 URL 导入 M3U/M3U8 播放列表
- 支持从本地文件导入播放列表
- 自动解析频道信息（名称、Logo、分组）
- 播放列表刷新和删除管理

### 🎬 视频播放
- 基于 Media3 ExoPlayer 的流畅播放
- 支持 HLS (.m3u8) 和 MPEG-TS (.ts) 流媒体协议
- 频道快速切换（左右滑动手势）
- 后台播放支持（MediaSessionService）
- 播放控制栏（播放/暂停、上一个/下一个）

### 🔍 频道浏览
- 频道列表展示（名称、Logo、分组、当前节目）
- 按分组筛选频道
- 关键词搜索频道
- 频道分组标签横向滚动

### ❤️ 收藏功能
- 一键收藏/取消收藏频道
- 收藏列表本地持久化
- 收藏页面快速访问

### 📋 EPG 节目指南
- XMLTV 格式 EPG 数据解析
- 显示当前正在播放的节目
- 显示下一个即将播放的节目
- 节目进度条显示
- 7 天节目预告
- 日期选择器切换

### 🎨 主题设置
- 自适应模式（跟随系统设置）
- 日间模式（浅色背景）
- 夜间模式（深色背景，护眼）
- 主题设置持久化

### ⚙️ 设置管理
- 播放列表管理（添加/刷新/删除）
- EPG 数据源配置
- EPG 数据同步
- 应用信息展示

---

## 🏗️ 技术架构

| 层级 | 技术 |
|------|------|
| 语言 | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| 架构 | MVVM + Clean Architecture |
| 播放器 | AndroidX Media3 ExoPlayer 1.5 |
| 数据库 | Room 2.6 |
| 网络 | Retrofit 2.9 + OkHttp 4.12 |
| 依赖注入 | Hilt 2.50 |
| 异步 | Kotlin Coroutines + Flow |
| 持久化 | DataStore |
| 图片加载 | Coil 2.7 |
| 后台任务 | WorkManager |

---

## 📱 系统要求

- **最低 Android 版本**: Android 8.0 (API 26)
- **目标 Android 版本**: Android 15 (API 35)
- **推荐设备**: 手机、平板

---

## 📦 安装说明

1. 从 [Releases](https://github.com/t-xue/MaxiTV/releases) 页面下载 `app-v1.0.0-release.apk`
2. 在 Android 设备上打开 APK 文件
3. 允许安装来自未知来源的应用（如需要）
4. 完成安装后打开应用

---

## 🔧 从源码构建

```bash
# 克隆仓库
git clone https://github.com/t-xue/MaxiTV.git
cd MaxiTV

# 切换到 v1.0.0 tag
git checkout v1.0.0

# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK
./gradlew assembleRelease
```

---

## 📖 使用指南

### 导入播放列表

1. 点击首页右下角的 **+** 按钮
2. 输入 M3U 播放列表 URL
3. 点击 **导入** 按钮

**推荐播放列表源：**
```
https://iptv-org.github.io/iptv/index.m3u
```

### 配置 EPG 数据源

1. 进入 **设置** → **节目指南 (EPG)**
2. 输入 XMLTV URL
3. 点击 **同步 EPG 数据**

**推荐 EPG 源：**
```
https://iptv-org.github.io/epg/guides/cn.xml
```

### 播放控制

| 操作 | 功能 |
|------|------|
| 左滑 | 切换到上一个频道 |
| 右滑 | 切换到下一个频道 |
| 上下滑 | 显示/隐藏控制栏 |
| 点击屏幕 | 显示频道信息 |

### 主题切换

1. 进入 **设置** → **外观设置**
2. 选择主题模式：
   - **自适应**: 跟随系统设置
   - **日间模式**: 浅色背景
   - **夜间模式**: 深色背景

---

## 🐛 已知问题

- 部分 RTMP 流可能需要额外支持
- UDP/RTP 组播需要网络权限配置

---

## 📝 后续计划

- [ ] Android TV 支持 (Leanback)
- [ ] 画中画 (PiP) 模式
- [ ] 投屏 (Chromecast/DLNA)
- [ ] 频道录制功能
- [ ] 多播放列表源管理
- [ ] 家长控制/频道锁定
- [ ] 自定义频道排序
- [ ] 多语言支持

---

## 🙏 致谢

- [ExoPlayer](https://github.com/google/ExoPlayer) - 媒体播放库
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代 UI 工具包
- [iptv-org](https://github.com/iptv-org/iptv) - IPTV 资源集合

---

## 📄 许可证

本项目基于 MIT 许可证开源 - 详见 [LICENSE](LICENSE) 文件

---

## 📞 反馈与支持

- **GitHub Issues**: [提交问题](https://github.com/t-xue/MaxiTV/issues)
- **GitHub Discussions**: [参与讨论](https://github.com/t-xue/MaxiTV/discussions)

---

**感谢使用 MaxiTV！** 🎉
