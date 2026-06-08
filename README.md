# MaxiTV

<div align="center">

**一个简洁、现代的 Android IPTV 播放器**

[![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)](https://github.com/t-xue/MaxiTV/releases/tag/v1.0.0)
[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.12-purple.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

## ✨ 功能特性

- 📺 **M3U 播放列表导入** - 支持从 URL 或本地文件导入 M3U/M3U8 播放列表
- 🔍 **频道分类与搜索** - 按分组浏览频道，支持关键词搜索
- ❤️ **收藏频道** - 收藏喜爱的频道，快速访问
- 📋 **EPG 节目指南** - 显示电子节目单，查看当前和即将播放的节目
- 🎬 **流畅播放** - 基于 Media3 ExoPlayer，支持 HLS、MPEG-TS 等流媒体协议
- 🎨 **现代 UI** - Material 3 设计，深色主题，流畅动画
- 📱 **手势控制** - 左右滑动切换频道，支持后台播放

## 📸 截图

<div align="center">
<img src="screenshots/home.png" width="200" alt="首页" />
<img src="screenshots/player.png" width="200" alt="播放器" />
<img src="screenshots/favorites.png" width="200" alt="收藏" />
<img src="screenshots/epg.png" width="200" alt="节目指南" />
</div>

## 🚀 快速开始

### 下载安装

从 [Releases](https://github.com/t-xue/MaxiTV/releases) 页面下载最新版本的 APK 安装。

### 从源码构建

1. **克隆仓库**
   ```bash
   git clone https://github.com/t-xue/MaxiTV.git
   cd MaxiTV
   ```

2. **使用 Android Studio 打开项目**
   - 打开 Android Studio
   - 选择 `File → Open`
   - 选择克隆的项目目录

3. **构建 APK**
   ```bash
   ./gradlew assembleDebug
   ```
   或在 Android Studio 中：`Build → Build Bundle(s) / APK(s) → Build APK(s)`

## 📖 使用指南

### 导入播放列表

1. 点击首页右下角的 **+** 按钮
2. 输入 M3U 播放列表 URL
3. 点击 **导入** 按钮

**示例播放列表源：**
```
https://iptv-org.github.io/iptv/index.m3u
```

### 配置 EPG 数据源

1. 进入 **设置** 页面
2. 在 **节目指南 (EPG)** 部分输入 XMLTV URL
3. 点击 **同步 EPG 数据**

**示例 EPG 源：**
```
https://iptv-org.github.io/epg/guides/cn.xml
```

### 播放控制

| 操作 | 功能 |
|------|------|
| 左滑 | 切换到上一个频道 |
| 右滑 | 切换到下一个频道 |
| 上下滑 | 显示/隐藏控制栏 |
| 点击 | 播放/暂停 |

## 🏗️ 技术架构

### 技术栈

| 层级 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| 架构 | MVVM + Clean Architecture |
| 播放器 | AndroidX Media3 ExoPlayer |
| 数据库 | Room |
| 网络 | Retrofit + OkHttp |
| 依赖注入 | Hilt |
| 异步 | Kotlin Coroutines + Flow |

### 项目结构

```
app/src/main/java/com/iptv/player/
├── MaxiTVApp.kt                    # Application 入口
├── MainActivity.kt                  # 主 Activity
├── di/                              # 依赖注入
├── data/                            # 数据层
│   ├── local/                       # Room 数据库
│   ├── remote/                      # 网络 API
│   ├── parser/                      # M3U/XMLTV 解析器
│   └── repository/                  # 数据仓库
├── domain/model/                    # 领域模型
├── ui/                              # UI 层
│   ├── home/                        # 首页
│   ├── player/                      # 播放器
│   ├── favorites/                   # 收藏
│   ├── epg/                         # 节目指南
│   └── settings/                    # 设置
└── worker/                          # 后台任务
```

## 🤝 贡献

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 开发环境

- Android Studio Hedgehog (2023.1.1) 或更新版本
- JDK 17
- Android SDK 35

## 📦 版本发布

| 版本 | 日期 | 说明 |
|------|------|------|
| [v1.0.0](https://github.com/t-xue/MaxiTV/releases/tag/v1.0.0) | 2024-06-08 | 首个正式版本，包含核心播放、收藏、EPG 功能 |

详细发布说明请查看 [RELEASE_NOTES.md](RELEASE_NOTES.md)

## 📝 待办事项

- [ ] Android TV 支持 (Leanback)
- [ ] 多播放列表源管理
- [ ] 家长控制/频道锁定
- [ ] 画中画 (PiP) 模式
- [ ] 投屏 (Chromecast/DLNA)
- [ ] 频道录制
- [ ] 自定义频道排序
- [ ] 多语言支持

## 📄 许可证

本项目基于 MIT 许可证开源 - 详见 [LICENSE](LICENSE) 文件

## 🙏 致谢

- [ExoPlayer](https://github.com/google/ExoPlayer) - 媒体播放库
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代 UI 工具包
- [iptv-org](https://github.com/iptv-org/iptv) - IPTV 资源集合

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐ Star 支持一下！**

</div>
