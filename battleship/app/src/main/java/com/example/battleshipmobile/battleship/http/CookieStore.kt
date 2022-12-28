package com.example.battleshipmobile.battleship.http

import android.content.Context
import android.util.Log
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * A cookie store interface that can be used to store cookies.
 */
interface CookieStore {
    operator fun set(url: HttpUrl, cookies: List<Cookie>)
    operator fun get(url: HttpUrl): List<Cookie>
    fun getAll(): List<Cookie>
    fun removeAll()
    fun isNotEmpty(): Boolean
    fun isEmpty(): Boolean
}

/**
 * A CookieStore implementation that stores cookies in the shared preferences.
 */
class SharedPrefsCookieStore(private val context: Context) : CookieStore {
    private val prefs by lazy {
        context.getSharedPreferences("CookieStorePrefs", Context.MODE_PRIVATE)
    }

    override fun set(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            prefs.edit().putString(url.host + " " + cookie.name, cookie.toString()).apply()
        }
    }

    override fun get(url: HttpUrl): List<Cookie> {
        val cookies = mutableListOf<Cookie>()
        prefs.all.forEach { (key, value) ->
            if (key.startsWith(url.host)) {
                val cookie = Cookie.parse(url, value.toString()) ?: return@forEach
                cookies.add(cookie)
            }
        }
        return cookies
    }

    override fun getAll(): List<Cookie> {
        val cookies = mutableListOf<Cookie>()
        prefs.all.forEach { (key, value) ->
            val url = key.split(" ")[0].toHttpUrl()
            val cookie = Cookie.parse(url, value.toString()) ?: return@forEach
            cookies.add(cookie)
        }
        return cookies
    }

    override fun removeAll() {
        prefs.edit().clear().apply()
    }

    override fun isNotEmpty(): Boolean =
        prefs.all.isNotEmpty()


    override fun isEmpty(): Boolean =
        prefs.all.isEmpty()


}