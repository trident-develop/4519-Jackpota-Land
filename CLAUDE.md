# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK (with ProGuard/R8)
./gradlew bundleRelease          # Build release AAB (strips ProGuard mapping via custom task)
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests (requires device/emulator)
./gradlew clean                  # Clean build outputs
```

## Project Overview

**Jackpota Land** — a slot machine casino game with six themed games (Aztec, Lion, Pirate, Royal, Seven, Treasure). Each theme has its own grid layout, symbol set, payout table, and win lines defined in `GameData.kt`.

Single `:app` module. Package: `com.centr`. Min SDK 28, Target SDK 36, Kotlin 2.3.20.

## Architecture (MVVM)

All source lives under `app/src/main/java/com/centr/`:

- **`data/`** — `GameData.kt` defines all game themes (grid dimensions, symbols, payouts, win lines). `PreferencesManager.kt` wraps SharedPreferences (`jackpota_prefs`) for coins, level, progress, daily bonuses, leaderboard.
- **`viewmodel/`** — `SlotViewModel` handles spin logic, win detection, betting, auto-spin, rewards. `MenuViewModel` manages home screen state (daily bonuses, gift box cooldowns, game selection). State exposed via `StateFlow`. UI state models in `viewmodel/model/`.
- **`ui/screens/`** — Composable screens. `SlotGameScreen.kt` (1177 lines) is the main game screen. `MenuScreen.kt` (681 lines) is the home/hub.
- **`ui/components/`** — Reusable composables (buttons, press effects, game grid components).
- **`ui/theme/`** — Material 3 theme with casino-style colors (gold, purple, crimson).
- **`navigation/`** — `Routes.kt` defines route constants. `MainNav.kt` is the primary nav graph. `LoadingNav.kt` handles splash flow.
- **`sound/`** — `SoundManager` manages background music and sound effects.

## Key Patterns

- **100% Jetpack Compose** — no XML layouts. Navigation via `androidx.navigation.compose`.
- **StateFlow + collectAsState()** — unidirectional data flow from ViewModels to Compose UI.
- **Two Activities**: `LoadingActivity` (portrait, splash screen entry point) → `MainActivity` (landscape, main game). Multi-touch blocked in `MainActivity.dispatchTouchEvent()` as anti-cheat.
- **Game themes are data-driven**: adding a new theme means adding a `GameTheme` entry in `GameData.kt` with grid config, symbols, payouts, and win lines.
- **Biased grid generation**: `SlotViewModel` has a 40% chance of generating near-win grids for engagement.

## Dependencies

Managed via Gradle version catalog (`gradle/libs.versions.toml`):
- Jetpack Compose BOM 2026.03.00, Material 3
- Firebase (Analytics, Crashlytics, Cloud Messaging)
- Google AdMob (test app ID currently configured)
- AndroidX Navigation, Lifecycle, Splash Screen

## Release Notes

- ProGuard/R8 enabled for release builds. Custom `removeProguardMap` task strips mapping from AAB.
- `android:allowBackup="false"`, cleartext traffic allowed.
