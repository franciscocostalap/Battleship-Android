package com.example.battleshipmobile.battleship.service.user

import com.example.battleshipmobile.battleship.http.buildRequest
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.battleship.service.Action
import com.example.battleshipmobile.battleship.service.RelationType
import com.example.battleshipmobile.battleship.service.buildAndSendRequest
import com.example.battleshipmobile.battleship.service.dto.UserDTO
import com.example.battleshipmobile.battleship.service.ensureAction
import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.*
import java.net.URL

/**
 * User Service
 * @property client Http client
 * @property jsonFormatter
 * @property rootUrl api base url used in all endpoints
 * @property parentUrl url that gives access to the requested resources with its siren actions/links
 */
class RealUserService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentUrl: URL
) : UserService {

    companion object {
        private const val LOGIN_REL = "login"
        private const val REGISTER_REL = "register"
        private const val HOME_ERR_MESSAGE = "Home is not set"
        private const val PROPERTIES_REQUIRED = "Response has no properties"
    }

    /**
     * Siren node entities
     */
    private var homeEntity: SirenEntity<Nothing>? = null

    /**
     * Sends the user credentials in the request body to the server.
     *
     * @param credentials username and password
     * @param action
     * @return [AuthInfo] user id and token
     */
    private suspend fun sendCredentials(credentials: User, action: Action): AuthInfo {
        val body = jsonFormatter.toJson(UserDTO(credentials))

        val result = buildAndSendRequest<AuthInfo>(
            client,
            jsonFormatter,
            action,
            body
        )

        return result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }

    /**
     * Registers a new user returning it's authentication information
     * @param user
     * @return [AuthInfo] user id and token
     */
    override suspend fun register(user: User): AuthInfo =
        sendCredentials(user, action = ensureRegisterAction())

    /**
     * Authenticates an already existing user returning it's authentication information
     * @param authenticator user
     * @return [AuthInfo] user id and token
     */
    override suspend fun login(authenticator: User): AuthInfo =
        sendCredentials(authenticator, action = ensureLoginAction())

    /**
     * Fetches and sets the home if it's not already set
     */
    private suspend fun fetchHomeEntity() {
        if (homeEntity != null) return

        val request = buildRequest(parentUrl)

        val responseResult = request.send(client) {
            handle<SirenEntity<Nothing>>(
                SirenEntity.getType<Unit>().type,
                jsonFormatter
            )
        }

        homeEntity = responseResult
    }

    /**
     * Ensures that the register action exists and returns it.
     * Requires that the home was previously fetched.
     *
     * @return [Action] Register url and method
     */
    private suspend fun ensureRegisterAction(): Action {
        fetchHomeEntity()
        val homeSirenEntity = homeEntity
        require(homeSirenEntity != null) { HOME_ERR_MESSAGE }

        return ensureAction(
            parentSirenEntity = homeSirenEntity,
            relation = REGISTER_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }

    /**
     * Ensures that the register action exists and returns it.
     * Requires that the home was previously fetched.
     *
     * @return [Action] Login url and method
     */
    private suspend fun ensureLoginAction(): Action {
        fetchHomeEntity()
        val homeSirenEntity = homeEntity
        require(homeSirenEntity != null) { HOME_ERR_MESSAGE }

        return ensureAction(
            parentSirenEntity = homeSirenEntity,
            relation = LOGIN_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }
}