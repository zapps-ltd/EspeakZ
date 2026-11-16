# EspeakZ

By the zapps.ltd developers, Harith and Mesar, ZAPPS SOFTWARE LTD.

EspeakZ is a text-to-speech engine for Android, made for screen reader users first, TalkBack users above all. It is a fork of the eSpeak NG Android app, written in Kotlin, and it speaks with the eSpeak NG speech engine, which supports more than 100 languages and accents. The speech is clear and stays clear at high speeds, and everything is spoken on the device.

EspeakZ is not the official eSpeak NG app and is not endorsed by the eSpeak NG project. It installs alongside the official app, because it has its own application id, com.espeakz.app.

## Features

### Speech engine and voices

- The eSpeak NG engine with its voice data for more than 100 languages and accents.
- Speech from the very first request after installing: the voice data installs the moment the app starts and the engine waits for it, so there is no install step and no need to turn the engine off and on.
- Klatt variant 6 as the default voice variant. A variant you choose, or the old default gender setting, still wins.
- Voice variants in eight groups: Male (1 to 8), Female (1 to 5), Klatt (1 to 6), NVDA (78 named variants such as Adam, Alex and Annie), Young, Old, Croak and Whisper. A variant eSpeak cannot load falls back to the plain voice instead of silence.
- A bass boost: a filter lifts the frequencies below 220 Hz by 8 dB for every voice and variant, without letting loud speech distort.
- High speech rates through the Sonic library, with an optional Rate boost (3x).
- Unicode normalization: stylized Unicode text, such as fancy fonts on social media, is read as normal words.

### Voice settings

- Speech rate, pitch, pitch variation and volume.
- Voice variant.
- Speak punctuation: All, None or Custom, with your own list of punctuation characters.
- Numbers longer than 6 digits, such as phone numbers, can be read one, two or three digits at a time.
- Import eSpeak dictionary: choose a dictionary file named like xx_dict with Android's file picker.
- A Test voice button in the per-language voice settings.

### Languages

- Voice languages: the first start installs the dictionaries for your device's languages and English only, in a fraction of a second. Add and remove more languages in Settings, Voice languages. Every change is copied next to the data first and switched into place only when complete.
- Languages that EspeakZ has but has not installed are reported to Android as such, and Android's install voice data button opens Voice languages.
- EspeakZ language: EspeakZ speaks in the device's language, plain English being American English, and in that language when an app asks for one EspeakZ does not have. Choose another language in Settings, EspeakZ language, and turn on Use EspeakZ language in all apps to use it whatever apps ask for.

### Dual languages

- Two languages are read at once, each by its own voice: one written in Latin letters, such as English or French, and one in another alphabet, such as Arabic.
- In Settings, Dual languages, one list holds every language that EspeakZ or any other installed engine speaks. Choosing a language puts it in use for its alphabet and shows its installed voices from every engine.
- Every language keeps the voice chosen for it, with its own speech rate, pitch and volume, so many languages can be set up and any two used.
- You choose the voice that reads numbers and punctuation, and the one that reads emojis.
- Another engine's audio joins EspeakZ's own stream, prepared a few parts ahead, so the two voices follow each other without gaps. An engine that cannot speak, or does not answer, is passed over and EspeakZ reads the text itself.

### Speed and reliability

- Fast start: the build writes an index of the voice data, so the app reads one small file instead of looking up hundreds of files. Starts after the first take about 15 milliseconds on a phone and under a tenth of a second on a OnePlus Watch 2.
- Quick answers: EspeakZ hands over its first 60 milliseconds of speech instead of waiting for 300, does not reload a voice that has not changed, and starts the engine off the main thread. With TalkBack the first sound of each request comes within a few milliseconds to a few hundredths of a second, and a stop takes effect at once.
- The native engine is started once per process and used by one caller at a time, so it is never restarted while it is speaking.
- Keep EspeakZ running asks Android to leave EspeakZ out of battery saving, on phones, tablets and TVs.
- EspeakZ speaks on the lock screen and before the first unlock after a restart.

### Devices

- Phones and tablets.
- Android TV and Fire TV, with a banner on the home screen and full control with a remote's D-pad.
- Wear OS watches, tested on a OnePlus Watch 2.
- A launcher icon on all of them, which opens the settings.

## Accessibility

- Made for screen reader users first, TalkBack above all. Everything in the app is meant to be fully usable with TalkBack, a keyboard and a TV remote's D-pad; this is still in progress.
- The voice settings sliders say their name and value once, Tab and Shift+Tab leave every list for its buttons, and lists open on the entry already chosen.
- Settings that read from the engine do so on a background thread, so the settings screen never waits for speech in progress.

## Privacy

- EspeakZ does not collect any data and does not connect to the internet for any reason. It does not ask for the internet permission.
- What EspeakZ reads, its settings and its voice data stay on your device.
- With Dual languages, EspeakZ hands the text in the second language to the engine whose voice you chose. That engine's own privacy terms apply to it, and a voice marked "needs internet" sends that text to its provider.
- The only special permission it asks for is the one behind Keep EspeakZ running, to be left out of battery saving.

## Requirements

- Android 5.0 (API level 21) or later.
- Built for the ABIs that Android NDK 29 builds by default: armeabi-v7a, arm64-v8a, x86 and x86_64.
- About the app version: 1.53.0, which follows the eSpeak NG version it started from.

## Installing

1. Install the EspeakZ APK from the releases page of https://github.com/zapps-ltd/EspeakZ.
2. In Android's settings, open Text-to-speech output (under Accessibility or System, depending on the device) and choose EspeakZ as the preferred engine.
3. Open EspeakZ's settings with its launcher icon or the settings gear beside Preferred engine in Android's Text-to-speech output, to add voice languages, choose a variant and set the rate.

## Building from source

From the project root, run `gradlew :app:assembleDebug` (`./gradlew :app:assembleDebug` on Linux and macOS). The build needs:

- JDK 17 or later.
- The Android SDK with platform 36, NDK 29.0.14206865 and CMake 3.22.1.
- git, to fetch the Sonic library during the first build.
- A C compiler for the computer you build on, used to compile the voice data: gcc or clang on Linux and macOS, llvm-mingw on Windows. On Windows the voice data step builds the engine with Ninja and the C compiler on PATH.

The debug APK is written to app/build/outputs/apk/debug. The app is the app module, the engine source is in the espeak-ng folder, the build file is Kotlin script, and the voice data is packed into the app as plain files.

## Interface languages

The app's own screens are in English, with partial translations, mainly the About screen and the older settings, into 46 languages: Afrikaans, Amharic, Arabic, Bulgarian, Catalan, Chinese (Simplified and Traditional), Croatian, Czech, Danish, Dutch, English (UK), Finnish, French, German, Greek, Hebrew, Hindi, Hungarian, Indonesian, Italian, Japanese, Korean, Latvian, Lithuanian, Malay, Norwegian Bokmål, Persian, Polish, Portuguese (Portugal and Brazil), Romanian, Russian, Serbian, Slovak, Slovenian, Spanish (Spain and the Americas), Swahili, Swedish, Tagalog, Thai, Turkish, Ukrainian, Vietnamese and Zulu. The speech languages are the eSpeak NG ones, a much longer list.

## Reporting problems and contact

Please report bugs and ideas by email to support@zapps.ltd or through the contact page of https://zapps.ltd. Pull requests are not accepted.

## Changes from eSpeak NG

EspeakZ started from eSpeak NG 1.53.0, upstream commit ba90c8e9f440ad544f674a790bb5f53878b6ffc5 of 22 September 2026. The features above that EspeakZ adds are: speech from the first request, Voice languages, Klatt variant 6 as the default, the Kotlin rewrite of the Android code, the faster start and quicker answers, the bass boost, Dual languages, EspeakZ language, long number grouping, Keep EspeakZ running, the screen reader and D-pad improvements, TV and watch support, and the About EspeakZ screen. Fixes: the Announcer and Marcelo voice variants pointed to files that do not exist and left the engine silent, two Robosoft variants were listed twice, and Import eSpeak dictionary saved files where the engine never looked. The complete list of changes is in the source history.

## License

EspeakZ is free software, licensed under the GNU General Public License, version 3 or (at your option) any later version (GPL-3.0-or-later), the same license as eSpeak NG. The full text is in the file COPYING. In short: you may use, study, change and share it, but every copy and every modified version must stay under the same license, must come with its complete source code, and must keep the copyright notices and the credits below. Nobody can make a closed version of it. Parts keep their own licenses: the Android app sources and the Sonic library are under the Apache License 2.0 (COPYING.APACHE), some eSpeak NG files under the 2-clause BSD license (COPYING.BSD2) and Unicode data under the Unicode license (COPYING.UCD). Copyright (C) 2026 ZAPPS SOFTWARE LTD.

EspeakZ comes with no warranty, to the extent permitted by law. LICENSE.txt has the copyright notice and the license of each part.

## Third-party software and credits

EspeakZ exists because of the work of the eSpeak NG project and everyone who contributed to it.

- eSpeak NG, https://github.com/espeak-ng/espeak-ng: the speech engine, the voice data for every language and the original Android app. Maintained by Reece H. Dunn and the eSpeak NG contributors. GPL-3.0-or-later.
- eSpeak, created by Jonathan Duddington, which eSpeak NG is based on.
- The eSpeak NG Android app, by Reece H. Dunn, Beka Gozalishvili, Alexandr Epaneshnikov and the eSpeak NG contributors, with code from Google Inc. and the Android Open Source Project. Apache License 2.0.
- The language maintainers and translators of eSpeak NG and its Android app, named in espeak-ng/ChangeLog.md and in the source files.
- Sonic, by Bill Cox, https://github.com/waywardgeek/sonic: speeds up speech. Apache License 2.0. It is fetched during the build.
- NV Speech Player, copyright 2014 NV Access Limited: the Klatt synthesizer inside eSpeak NG. GPL-3.0-or-later.
- getopt.c, from NetBSD, used only when building the engine for Windows: 2-clause BSD license.
- Unicode Character Database files used by the engine: the Unicode license.
- Kotlin standard library, by JetBrains and the Kotlin contributors, https://kotlinlang.org: built into the app. Apache License 2.0.
- Build and test only, not part of the app: the Android Gradle Plugin 9.3.0 (Apache License 2.0), JUnit 4.13.2 (Eclipse Public License 1.0), Hamcrest 2.2 (BSD 3-clause) and AndroidX Test runner, core and JUnit extension (Apache License 2.0). The app itself uses the Android platform libraries and the Kotlin standard library (by JetBrains, Apache License 2.0), which is built into the app; it has no other library dependency.

Android is a trademark of Google LLC.
