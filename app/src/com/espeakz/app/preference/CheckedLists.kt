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

package com.espeakz.app.preference

import android.view.KeyEvent
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Checkable
import android.widget.ListView

/**
 * Makes a screen reader say the new state when an entry of a multiple-choice
 * list is checked or unchecked.
 *
 * ListView records the change at once but redraws the entry only at its next
 * layout, after the click has already been reported to accessibility with the
 * old state; TalkBack on Wear OS then says nothing. The item click listener
 * runs before that report, so setting the entry's state there makes the
 * report carry the new one.
 */
internal fun ListView.reportCheckedStateOnClick() {
    setOnItemClickListener { _, view, position, _ ->
        (view as? Checkable)?.isChecked = isItemChecked(position)
    }
}

/**
 * Opens a dialog's list on its entry [index], the current choice: scrolled
 * to the top of the list, with the keyboard, the D-pad and a screen reader
 * all starting there, so that moving back and forward from it reaches every
 * other entry.
 *
 * A watch shows the entry below the top of its round screen, with the entry
 * before it above; a screen reader left to itself then starts on that
 * earlier entry, which is not the checked one, so its focus is put on the
 * entry here. Any header rows a dialog adds to its list are counted.
 */
internal fun ListView.openOn(index: Int) {
    if (index < 0) return
    post {
        val position = index + headerViewsCount
        // The first entry stays below the title a watch shows above it.
        if (index > 0) setSelection(position)
        requestFocus()
        // Once the list is laid out at its new place.
        postDelayed({
            getChildAt(position - firstVisiblePosition)
                ?.performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null)
        }, SCREEN_READER_FOCUS_DELAY_MS)
    }
}

private const val SCREEN_READER_FOCUS_DELAY_MS = 350L

/**
 * Lets Tab and Shift+Tab leave a list for the controls around it, such as a
 * dialog's buttons. A ListView otherwise moves through its entries on Tab,
 * so a keyboard user only got out with End and then Down. Arrows still move
 * through the entries.
 */
internal fun ListView.leaveOnTab() {
    setOnKeyListener { list, keyCode, event ->
        if (keyCode != KeyEvent.KEYCODE_TAB) return@setOnKeyListener false
        if (event.action == KeyEvent.ACTION_DOWN) {
            val direction = if (event.isShiftPressed) View.FOCUS_BACKWARD else View.FOCUS_FORWARD
            list.focusSearch(direction)?.requestFocus()
        }
        true
    }
}
