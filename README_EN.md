# MaxiTV

<div align="center">

[中文](README.md) | English

</div>

<div align="center">

**A simple and modern Android IPTV Player**

[![Version](https://img.shields.io/badge/Version-1.1.0-blue.svg)](https://github.com/t-xue/MaxiTV/releases/tag/v1.1.0)
[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.12-purple.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

## ✨ Features

- 📺 **M3U Playlist Import** - Import M3U/M3U8 playlists from URL or local file
- 🔍 **Channel Categories & Search** - Browse channels by group, keyword search supported
- ❤️ **Favorite Channels** - Bookmark your favorite channels for quick access
- 📋 **EPG Program Guide** - View electronic program guide with current and upcoming shows
- 🎬 **Smooth Playback** - Based on Media3 ExoPlayer, supports HLS, MPEG-TS and more
- 🎨 **Modern UI** - Material 3 design, dark theme, smooth animations
- 📱 **Gesture Controls** - Swipe left/right to switch channels, background playback supported

## 📸 Screenshots

<div align="center">
<img src="screenshots/home.png" width="200" alt="Home" />
<img src="screenshots/player.png" width="200" alt="Player" />
<img src="screenshots/favorites.png" width="200" alt="Favorites" />
<img src="screenshots/epg.png" width="200" alt="EPG" />
</div>

## 🚀 Quick Start

### Download

Download the latest APK from [Releases](https://github.com/t-xue/MaxiTV/releases) page.

### Build from Source

1. **Clone Repository**
   ```bash
   git clone https://github.com/t-xue/MaxiTV.git
   cd MaxiTV
   ```

2. **Open with Android Studio**
   - Open Android Studio
   - Select `File → Open`
   - Choose the cloned project directory

3. **Build APK**
   ```bash
   ./gradlew assembleDebug
   ```
   Or in Android Studio: `Build → Build Bundle(s) / APK(s) → Build APK(s)`

## 📖 User Guide

### Import Playlist

1. Tap the **+** button at the bottom right of the home screen
2. Enter M3U playlist URL
3. Tap **Import** button

**Example Playlist Sources:**
```
https://iptv-org.github.io/iptv/index.m3u
```

### Configure EPG

1. Go to **Settings** page
2. Enter XMLTV URL in **Program Guide (EPG)** section
3. Tap **Sync EPG Data**

**Example EPG Sources:**
```
https://iptv-org.github.io/epg/guides/us.xml
```

### Playback Controls

| Action | Function |
|--------|----------|
| Swipe Left | Switch to previous channel |
| Swipe Right | Switch to next channel |
| Swipe Up/Down | Show/Hide controls |
| Tap | Play/Pause |

## 🏗️ Architecture

### Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| Player | AndroidX Media3 ExoPlayer |
| Database | Room |
| Network | Retrofit + OkHttp |
| DI | Hilt |
| Async | Kotlin Coroutines + Flow |

### Project Structure

```
app/src/main/java/com/iptv/player/
├── MaxiTVApp.kt                    # Application entry
├── MainActivity.kt                  # Main Activity
├── di/                              # Dependency Injection
├── data/                            # Data Layer
│   ├── local/                       # Room Database
│   ├── remote/                      # Network API
│   ├── parser/                      # M3U/XMLTV Parsers
│   └── repository/                  # Repositories
├── domain/model/                    # Domain Models
├── ui/                              # UI Layer
│   ├── home/                        # Home Screen
│   ├── player/                      # Player
│   ├── favorites/                   # Favorites
│   ├── epg/                         # EPG Guide
│   └── settings/                    # Settings
└── worker/                          # Background Tasks
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Create a Pull Request

### Development Environment

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 35

## 📦 Releases

| Version | Date | Description |
|---------|------|-------------|
| [v1.1.0](https://github.com/t-xue/MaxiTV/releases/tag/v1.1.0) | 2026-06-09 | Custom channels, local file import, landscape toggle |
| [v1.0.0](https://github.com/t-xue/MaxiTV/releases/tag/v1.0.0) | 2026-06-08 | Initial release with core playback, favorites, EPG |

For detailed release notes, see [RELEASE_NOTES.md](RELEASE_NOTES.md)

## 📝 Roadmap

- [ ] Android TV Support (Leanback)
- [ ] Multi-playlist Management
- [ ] Parental Controls / Channel Lock
- [ ] Picture-in-Picture (PiP) Mode
- [ ] Casting (Chromecast/DLNA)
- [ ] Channel Recording
- [ ] Custom Channel Sorting
- [ ] Multi-language Support

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file

## 🙏 Acknowledgements

- [ExoPlayer](https://github.com/google/ExoPlayer) - Media playback library
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- [iptv-org](https://github.com/iptv-org/iptv) - IPTV resources collection

---

<div align="center">

**If you find this project helpful, please give it a ⭐ Star!**

</div>
