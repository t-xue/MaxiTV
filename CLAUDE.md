# MaxiTV 프로젝트 가이드

## 프로젝트 개요
MaxiTV는 Android IPTV 플레이어 앱입니다. Kotlin + Jetpack Compose + MVVM 아키텍처를 사용합니다.

## 기술 스택
- **언어**: Kotlin 2.0
- **UI**: Jetpack Compose + Material 3
- **아키텍처**: MVVM + Clean Architecture
- **데이터**: Room + DataStore
- **DI**: Hilt
- **비동기**: Coroutines + Flow

## 하네스: MaxiTV 기능 개발

**목표:** MaxiTV 앱의 새 기능 개발을 에이전트 팀으로 조율

**트리거:** "기능 추가", "새 기능", "채널 추가", "로컬 파일", "앱 이름", "개발" 등의 키워드로 `maxitv-feature-dev` 스킬을 사용하라. 단순 질문은 직접 응답 가능.

**변경 이력:**
| 날짜 | 변경 내용 | 대상 | 사유 |
|------|----------|------|------|
| 2024-06-08 | 초기 구성 | 전체 | 하네스 구축 |

## 프로젝트 구조
```
app/src/main/java/com/iptv/player/
├── MaxiTVApp.kt          # Application
├── MainActivity.kt        # Main Activity
├── di/                    # 의존성 주입
├── data/                  # 데이터 레이어
│   ├── local/             # Room DB
│   ├── remote/            # 네트워크
│   ├── parser/            # M3U/XMLTV 파서
│   └── repository/        # Repository
├── domain/model/          # 도메인 모델
├── ui/                    # UI 레이어
│   ├── home/              # 홈 화면
│   ├── player/            # 플레이어
│   ├── favorites/         # 즐겨찾기
│   ├── epg/               # EPG
│   └── settings/          # 설정
└── worker/                # 백그라운드 작업
```

## 빌드 명령어
```bash
# 디버그 빌드
./gradlew assembleDebug

# 릴리스 빌드
./gradlew assembleRelease

# 테스트
./gradlew test
```
