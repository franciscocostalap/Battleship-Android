package com.example.battleshipmobile.battleship.http

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * CookieJar implementation that allows to resend the received back cookies to the server.
 * This is needed because the server uses cookies for authentication.
 */
class ResendCookiesJar(private val cookieStore : CookieStore) : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url]
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url] = cookies;
    }

}