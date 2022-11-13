package com.example.battleshipmobile.battleship.service.user

import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.*
import java.net.URL

class RealUserService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val host: String,
    private val homeURL: URL
) : UserService {

    private suspend fun sendCredentials(credentials: User, action: Action): AuthInfo {
        val body = jsonFormatter.toJson(credentials)

        val request = buildRequest(action.url, action.method, body)

        val result = request.send(client) {
            handle<SirenEntity<AuthInfo>>(
                SirenEntity.getType<AuthInfo>().type,
                jsonFormatter
            )
        }

        return result.properties ?: throw IllegalStateException("No properties in response")
    }

    /**
     * Registers a new user returning it's authentication information
     */
    override suspend fun register(user: User): AuthInfo =
        sendCredentials(user, action = ensureRegisterAction())

    /**
     * Authenticates an already existing user returning it's authentication information
     */
    override suspend fun login(authenticator: User): AuthInfo =
        sendCredentials(authenticator, action = ensureLoginAction())


    private data class Action(val url: URL, val method: HttpMethod)
    private var registerAction: SirenAction? = null
    private var loginAction: SirenAction? = null

    private suspend fun ensureRegisterAction(): Action = ensureAction{ registerAction }
    private suspend fun ensureLoginAction(): Action = ensureAction { loginAction }

    private suspend fun ensureAction(action: () -> SirenAction?): Action{
        if (action() == null) fillServiceUrls()

        val ensuredAction = action() ?: throw IllegalStateException("No action")

        val newUrl = URL(host + ensuredAction.href)
        val method = ensuredAction.method?.let { HttpMethod.valueOf(it) }
            ?: throw IllegalStateException("No register action") // TODO: TIRAR ESTES ERROS MANHOSOS

        return Action(newUrl, method)
    }

    private suspend fun fillServiceUrls() {
        val request = buildRequest(homeURL)
        val result = request.send(client) {
            handle<SirenEntity<Any>>(
                SirenEntity.getType<Any>().type,
                jsonFormatter
            )
        }

        result.actions?.forEach { action ->
            when (action.name) {
                "register" -> registerAction = action
                "login" -> loginAction = action
            }
        }
    }

}