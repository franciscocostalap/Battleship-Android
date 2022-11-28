package com.example.battleshipmobile.battleship.auth

import android.content.Context
import com.example.battleshipmobile.battleship.service.user.AuthInfo


/**
 * Contract for the auth information repository implementations.
 */
interface AuthInfoRepository{

    /**
     * The auth information if already stored, or null otherwise.
     * Accesses to this property CAN be made from the UI thread.
     */
    var authInfo: AuthInfo?

}

/**
 * An auth information repository that stores the user information in the shared preferences.
 */
class AuthInfoRepositorySharedPrefs(private val context: Context) : AuthInfoRepository {

    companion object{
        private const val ID_KEY = "ID"
        private const val TOKEN_KEY = "TOKEN"
    }

    private val prefs by lazy {
        context.getSharedPreferences("AuthInfoPrefs", Context.MODE_PRIVATE)
    }

    /**
     * The auth information if already stored, or null otherwise.
     * Accesses to this property CAN be made from the UI thread.
     */
    override var authInfo: AuthInfo?
        get(){
            val id = prefs.getString(ID_KEY, null)
            val token = prefs.getString(TOKEN_KEY, null)

            if(id == null || token == null)
                return null

            return AuthInfo(id.toInt(), token)
        }
        set(value) {
            if(value == null)
                prefs.edit()
                    .remove(ID_KEY)
                    .remove(TOKEN_KEY)
                    .apply()
            else
                prefs.edit()
                    .putString(ID_KEY, value.uid.toString())
                    .putString(TOKEN_KEY, value.token)
                    .apply()
        }





}