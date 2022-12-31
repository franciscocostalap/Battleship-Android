package com.example.battleshipmobile.battleship.auth

import android.content.Context
import android.util.Log
import com.example.battleshipmobile.battleship.http.CookieStore
import com.example.battleshipmobile.battleship.service.ID
import okhttp3.HttpUrl.Companion.toHttpUrl


/**
 * Contract for the auth information repository implementations.
 */
interface IAuthInfoService{

    /**
     * The user Id if already stored, or null otherwise.
     * Accesses to this property CAN be made from the UI thread.
     */
    var uid: ID?

}

/**
 * An auth information repository that stores the user information in the shared preferences.
 */
class AuthInfoService(
    private val context: Context,
    private val cookieStore: CookieStore,
    private val host : String

    ) : IAuthInfoService {

    companion object{
        private const val ID_KEY = "ID"
        private const val NAME_KEY = "NAME"
    }

    private val prefs by lazy {
        context.getSharedPreferences("AuthInfoPrefs", Context.MODE_PRIVATE)
    }

    fun clearAuthInfo(){
        Log.v("AuthInfoService", "Clearing auth info")
        cookieStore.removeAll()
        Log.v("AuthInfoService", "id:${uid}")
        prefs.edit().clear().apply()
        Log.v("AuthInfoService", "id:${uid}")
    }

    fun hasAuthInfo(): Boolean {
        Log.v("AuthInfoService", "Has auth info: ${uid}")
        return cookieStore[host.toHttpUrl()].isNotEmpty() && uid != null
    }

    /**
     * The auth information if already stored, or null otherwise.
     * Accesses to this property CAN be made from the UI thread.
     */
    override var uid: ID?
        get(){
            val id = prefs.getString(ID_KEY, null) ?: return null

            return id.toInt()
        }
        set(value) {
            if(value == null)
                prefs.edit()
                    .remove(ID_KEY)
                    .apply()
            else
                prefs.edit()
                    .putString(ID_KEY, value.toString())
                    .apply()
        }





}