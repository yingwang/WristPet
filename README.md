# WristPet

A pixel-art virtual pet for Wear OS. Raise a tiny creature on your wrist — feed it with attention and steps.

## Features

**Pet States**
- **Happy** — green, smiling, all is well
- **Bored** — 2 hours without interaction, half-lidded eyes
- **Angry** — 6 hours ignored, turns red
- **Sleeping** — 10 PM to 7 AM, recharges energy
- **Sick** — health drops below 30

**Interaction**
- Tap the pet to boost happiness (+15) and energy (+5)
- Walk to keep it happy — 6,000 daily steps = +20 happiness bonus
- Stats decay over time: happiness fades, energy drains

**Watch Face Integration**
- **Complication**: Add to any watch face slot — shows pet state + happiness percentage, or a small pet image
- **Tile**: Swipe from watch face to see pet image, mood, happiness bar, and step count. Tap to open the app

**Background Updates**
- Passive step tracking via Health Services — updates in near real-time
- WorkManager job every 15 minutes to refresh pet state
- Complication and tile auto-refresh when steps change

## Install

```bash
# Build
./gradlew assembleRelease

# Install to connected watch
adb install app/build/outputs/apk/release/app-release.apk
```

## Tech Stack

- Kotlin + Jetpack Compose (Wear OS)
- Room (persistence)
- Health Services (step counting)
- WorkManager (background updates)
- Wear Tiles API
- Complications Data Source API

## Requirements

- Wear OS 3.0+ (API 30+)
- Tested on Pixel Watch 4
