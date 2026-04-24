# WristPet

A pixel-art virtual pet for Wear OS. Raise a tiny creature on your wrist — keep it happy by staying active.

## Features

**Pet States**
- **Happy** — green, smiling, all is well
- **Bored** — 2 hours of inactivity, half-lidded eyes
- **Angry** — 6 hours of inactivity, turns red
- **Sleeping** — 10 PM to 7 AM, recharges energy
- **Sick** — health drops below 30

**Stay Active, Stay Happy**
- Walking counts as interaction — your pet won't get bored or angry as long as you're moving
- Every 100 steps gives a happiness boost
- 6,000 daily steps = full step bonus (+20 happiness)
- Tap the pet in the app for an extra boost (+15 happiness, +5 energy)
- Stats decay over time without activity

**Watch Face Complication**
- Add the pet directly to your watch face — it changes color and expression with its mood
- Tap the complication to interact without opening the app
- Updates automatically when you walk

**Tile**
- Swipe from watch face to see pet image, mood, happiness bar, and step count
- Tap to open the full app

**Background Updates**
- Passive step tracking via Health Services — near real-time updates
- WorkManager job every 15 minutes as fallback
- Complication and tile refresh when steps change

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
