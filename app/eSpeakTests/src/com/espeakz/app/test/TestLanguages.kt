/*
 * Copyright (C) 2026 ZAPPS SOFTWARE LTD
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

package com.espeakz.app.test

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.espeakz.app.EspeakData
import com.espeakz.app.LanguageSettings
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

/** Shared setup for the tests. */
object TestLanguages {
    val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    /** Device-protected storage, where EspeakZ keeps its settings and voice data. */
    val storage: Context
        get() = EspeakData.storage(targetContext)

    /**
     * Installs every language and turns every voice on. EspeakZ installs only
     * the device's language at first, with one voice of it on, but most tests
     * check the full list of voices.
     */
    @JvmStatic
    fun installAll() {
        assertThat(
            "installing every language",
            EspeakData.setInstalledDictionaries(storage, EspeakData.availableDictionaries(storage)),
            `is`(true),
        )
        @Suppress("DEPRECATION")
        android.preference.PreferenceManager.getDefaultSharedPreferences(storage).edit()
            .remove(LanguageSettings.PREF_SUPPORTED_LANGUAGES).commit()
    }
}
