/*
 * Copyright (C) 2026 ZAPPS SOFTWARE LTD
 * Copyright (C) 2025
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.espeakz.app

import android.content.Context
import android.content.SharedPreferences

/** Which installed voices are reported to Android, as chosen in Supported languages. */
object LanguageSettings {
    const val PREF_SUPPORTED_LANGUAGES = "espeak_supported_languages"

    /** The selected voices, or null when all are selected. */
    @JvmStatic
    fun getSelectedLanguages(preferences: SharedPreferences): Set<String>? {
        if (!preferences.contains(PREF_SUPPORTED_LANGUAGES)) {
            return null // treat as "all"
        }
        val selected = preferences.getStringSet(PREF_SUPPORTED_LANGUAGES, null)
        if (selected.isNullOrEmpty()) {
            return null // missing or empty means all
        }
        return HashSet(selected)
    }

    @JvmStatic
    fun isVoiceEnabled(voice: Voice, preferences: SharedPreferences): Boolean {
        val selected = getSelectedLanguages(preferences)
        return selected == null || voice.toString() in selected
    }

    @JvmStatic
    fun filterVoices(voices: List<Voice>, preferences: SharedPreferences): List<Voice> {
        val selected = getSelectedLanguages(preferences) ?: return voices
        return voices.filter { it.toString() in selected }
    }

    /**
     * Turns on [voice] alone, on a first install, so that Android and the
     * apps see one voice of one language; Voice languages and Supported
     * languages add more. A choice already made is kept.
     */
    @JvmStatic
    fun selectOnly(preferences: SharedPreferences, voice: Voice) {
        if (preferences.contains(PREF_SUPPORTED_LANGUAGES)) return
        preferences.edit().putStringSet(PREF_SUPPORTED_LANGUAGES, setOf(voice.toString())).commit()
    }

    /**
     * The languages in use, by dictionary: installed, with a voice turned on.
     * English is always installed, since the engine needs it, but it is in
     * use only when one of its voices is on.
     */
    @JvmStatic
    fun languagesInUse(storage: Context, preferences: SharedPreferences): Set<String> {
        val installed = EspeakData.installedDictionaries(storage)
        val selected = getSelectedLanguages(preferences) ?: return installed
        return installed.filter { dictionary ->
            EspeakData.voicesOf(storage, dictionary).any { it.toString() in selected }
        }.toSet()
    }

    /**
     * Keeps the voices in step with Voice languages: a language added turns
     * on its one usual voice, and a language removed turns off its voices.
     * When nothing is left, American English is turned on, so that EspeakZ
     * always has a voice. With every voice on, nothing changes.
     */
    @JvmStatic
    fun applyLanguages(storage: Context, preferences: SharedPreferences, before: Set<String>, chosen: Set<String>) {
        val selected = getSelectedLanguages(preferences)?.toMutableSet() ?: return
        for (dictionary in before - chosen) {
            selected.removeAll(EspeakData.voicesOf(storage, dictionary).map { it.toString() }.toSet())
        }
        for (dictionary in chosen - before) {
            EspeakData.defaultVoiceFor(storage, dictionary)?.let { selected.add(it.toString()) }
        }
        if (selected.isEmpty()) {
            EspeakData.defaultVoiceFor(storage, EspeakData.ENGLISH)?.let { selected.add(it.toString()) }
        }
        preferences.edit().putStringSet(PREF_SUPPORTED_LANGUAGES, selected).commit()
    }
}
