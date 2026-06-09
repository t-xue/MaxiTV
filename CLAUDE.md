# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run all tests
./gradlew test

# Run unit tests only
./gradlew testDebugUnitTest

# Clean build
./gradlew clean assembleDebug

# Lint check
./gradlew lint
```

Output APK: `app/build/outputs/apk/debug/app-debug.apk`

## Tech Stack

- **Language**: Kotlin 2.0, JDK 17
- **UI**: Jetpack Compose + Material 3 (dark theme default)
- **Architecture**: MVVM + Clean Architecture
- **Player**: AndroidX Media3 ExoPlayer 1.5
- **Database**: Room 2.6
- **DI**: Hilt 2.50
- **Network**: Retrofit + OkHttp
- **Async**: Kotlin Coroutines + Flow

## Architecture

The app follows Clean Architecture with three layers:

```
ui/ → domain/model/ ← data/
(ViewModels)          (Repositories → DAOs → Database)
```

**Data flow**: UI (Compose) → ViewModel → Repository → Room/Retrofit

### Key Patterns

- **DI Modules** (`di/`): `AppModule` (ExoPlayer, ThemeManager), `DatabaseModule` (Room), `NetworkModule` (Retrofit)
- **Database** (`data/local/`): `MaxiTVDatabase` (version 3) with 4 entities: `ChannelEntity`, `PlaylistEntity`, `EpgProgramEntity`, `CustomChannelEntity`
- **Parsers** (`data/parser/`): `M3uParser` for playlist import, `XmlTvParser` for EPG data
- **Repositories** (`data/repository/`): Abstract data sources, combine local DB with remote API

### Channel System

Two independent channel sources:
1. **Playlist channels** (`ChannelEntity`): Imported from M3U URLs, stored via `ChannelRepository`
2. **Custom channels** (`CustomChannelEntity`): Manually added one-by-one, stored via `CustomChannelRepository`

Custom channels use ID offset (+1000000) to avoid conflicts with playlist channels in the UI layer.

### Player Architecture

- `ExoPlayer` is a singleton provided by `AppModule`
- `PlayerViewModel` handles playback and channel switching
- Custom User-Agent/Referer headers are set per-channel for streams that require them
- URL encoding uses `java.net.URI` for Chinese characters
- Portrait: `RESIZE_MODE_FIT`, Landscape: `RESIZE_MODE_ZOOM`

### Theme System

`ThemeManager` uses DataStore to persist theme preference (SYSTEM/LIGHT/DARK). `MainActivity` reads the Flow and passes to `MaxiTVTheme`.

## Android SDK Setup

The project requires Android SDK. Set `sdk.dir` in `local.properties` or `ANDROID_HOME` environment variable.

SDK components needed:
```
platforms;android-35
build-tools;35.0.0
platform-tools
```

## Common Issues

- **Build fails with SDK not found**: Create `local.properties` with `sdk.dir=C:\\android-sdk` (or your SDK path)
- **WorkManager initializer error**: The manifest uses Hilt's `Configuration.Provider`, so the default `InitializationProvider` must be removed with `tools:node="remove"`
- **Chinese URLs fail to play**: Ensure URL encoding via `java.net.URI.toASCIIString()`
