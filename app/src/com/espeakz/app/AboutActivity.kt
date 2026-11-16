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

/*
 * The About EspeakZ screen, as one page in the zapps.ltd look: the author,
 * the fork, its changes, the credits, the license notices the GPL asks an
 * interactive program to show, the developers team (in a random order each
 * time it opens), the contact address and the home page. Each entry is its
 * own list item, so a screen reader stops on each one. The links open in
 * the mail program or the browser; the page itself loads nothing.
 */

package com.espeakz.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlin.random.Random

class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val web = WebView(this)
        web.webViewClient = object : WebViewClient() {
            @Deprecated("Kept for Android versions before 7.0")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                openLink(url)
                return true
            }
        }
        setContentView(web)
        web.loadDataWithBaseURL(null, buildPage(), "text/html", "utf-8", null)
    }

    private fun openLink(url: String) {
        val uri = Uri.parse(url)
        val intent = if (url.startsWith("mailto:")) Intent(Intent.ACTION_SENDTO, uri)
            else Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // No mail program or browser on this device: nothing to open.
        }
    }

    private fun esc(text: String): String = TextUtils.htmlEncode(text)

    private fun items(arrayId: Int): String =
        resources.getStringArray(arrayId).joinToString("", "<ul>", "</ul>") { "<li>" + esc(it) + "</li>" }

    private fun section(headingId: Int, body: String): String =
        "<section><h2>" + esc(getString(headingId)) + "</h2>" + body + "</section>"

    private fun buildPage(): String {
        val names = if (Random.nextBoolean()) listOf("Harith", "Mesar") else listOf("Mesar", "Harith")
        val email = "support@zapps.ltd"
        val page = StringBuilder()
        page.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\">")
        page.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
        page.append("<style>").append(CSS).append("</style></head><body><main>")
        page.append("<header class=\"hero\"><h1>").append(esc(getString(R.string.about_title))).append("</h1>")
        page.append("<p class=\"by\">").append(esc(getString(R.string.about_author))).append("</p>")
        page.append("<p>").append(esc(getString(R.string.about_version, BuildConfig.VERSION_NAME))).append("</p></header>")
        page.append("<section><p>").append(esc(getString(R.string.about_intro))).append("</p></section>")
        page.append(section(R.string.about_privacy_heading, items(R.array.about_privacy)))
        page.append(section(R.string.about_goals_heading, items(R.array.about_goals)))
        page.append(section(R.string.about_changes_heading, items(R.array.about_changes)))
        page.append(section(R.string.about_credits_heading, items(R.array.about_credits)))
        page.append(section(R.string.about_license_heading, items(R.array.about_license)))
        page.append(section(R.string.about_team_heading,
            "<p>" + esc(getString(R.string.about_team, names[0], names[1])) + "</p>"))
        page.append(section(R.string.about_contact_heading,
            "<p class=\"buttons\"><a class=\"button\" href=\"mailto:" + email + "\">" +
                esc(getString(R.string.about_email, email)) + "</a> " +
                "<a class=\"button secondary\" href=\"https://zapps.ltd\">" +
                esc(getString(R.string.about_home)) + "</a></p>"))
        page.append("<footer><p>").append(esc(getString(R.string.about_copyright))).append("</p></footer>")
        page.append("</main></body></html>")
        return page.toString()
    }

    private companion object {
        const val CSS = """:root { color-scheme: light dark; --bg: #f4f6fb; --card: #ffffff;
  --text: #1a1e29; --muted: #4b5366; --accent: #1d5bbf; --on-accent: #ffffff;
  --border: #d8dce6; --focus: #ffb000; }
@media (prefers-color-scheme: dark) { :root { --bg: #11141b; --card: #1a1f2c;
  --text: #eef1f7; --muted: #bcc3d2; --accent: #8ab4ff; --on-accent: #0b1220;
  --border: #2c3346; } }
* { box-sizing: border-box; }
body { margin: 0; background: var(--bg); color: var(--text);
  font: 17px/1.6 "Segoe UI", system-ui, -apple-system, Roboto, sans-serif; }
main { max-width: 46rem; margin: 0 auto; padding: 1.5rem 1.1rem 2.5rem; }
.hero { background: linear-gradient(135deg, #1d5bbf, #6a3fc2); color: #ffffff;
  border-radius: 18px; padding: 1.6rem 1.5rem; margin-bottom: 1.25rem; }
.hero h1 { margin: 0 0 .35rem; font-size: 1.9rem; line-height: 1.25; }
.hero p { margin: .15rem 0; }
.hero .by { font-size: 1.15rem; font-weight: 600; }
section { background: var(--card); border: 1px solid var(--border);
  border-radius: 14px; padding: 1.1rem 1.4rem; margin: 0 0 1rem; }
h2 { margin: 0 0 .6rem; font-size: 1.3rem; color: var(--accent); }
ul { margin: .3rem 0; padding-inline-start: 1.3rem; }
li { margin-bottom: .4rem; }
a { color: var(--accent); }
.buttons { display: flex; flex-wrap: wrap; gap: .6rem; margin: .4rem 0 0; }
a.button { display: inline-block; background: var(--accent); color: var(--on-accent);
  padding: .65rem 1.15rem; border-radius: 10px; text-decoration: none; font-weight: 600; }
a.button.secondary { background: transparent; color: var(--accent);
  border: 2px solid var(--accent); }
a:focus-visible { outline: 3px solid var(--focus); outline-offset: 3px; }
footer { color: var(--muted); text-align: center; font-size: .95rem; margin-top: 1.4rem; }
@media (forced-colors: active) { .hero { background: Canvas; color: CanvasText;
  border: 1px solid CanvasText; } a.button { border: 2px solid LinkText; } }"""
    }
}
