package com.example.battleshipmobile.battleship.service.user

import com.example.battleshipmobile.battleship.service.Relation
import com.example.battleshipmobile.battleship.service.RelationType
import com.example.battleshipmobile.battleship.service.buildAndSendRequest
import com.example.battleshipmobile.battleship.service.dto.InputUserDTO
import com.example.battleshipmobile.utils.*
import com.example.battleshipmobile.battleship.service.ensureRelation
import com.google.gson.Gson
import okhttp3.*
import java.net.URL

/**
 * User Service
 * @property client Http client
 * @property jsonFormatter
 * @property rootUrl api base url used in all endpoints
 * @property parentURL url that gives access to the requested resources with its siren actions/links
 */
class RealUserService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentURL: URL
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
    private suspend fun sendCredentials(credentials: User, relation: Relation): AuthInfo {
        val body = jsonFormatter.toJson(InputUserDTO(credentials))

        val result = buildAndSendRequest<AuthInfo>(
            client,
            jsonFormatter,
            relation,
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
        sendCredentials(user, relation = ensureRegisterAction())

    /**
     * Authenticates an already existing user returning it's authentication information
     * @param authenticator user
     * @return [AuthInfo] user id and token
     */
    override suspend fun login(authenticator: User): AuthInfo =
        sendCredentials(authenticator, relation = ensureLoginAction())


    /**
     * Ensures that the register action exists and returns it.
     * Requires that the home was previously fetched.
     *
     * @return [Action] Register url and method
     */
    private suspend fun ensureRegisterAction(): Relation {
        homeEntity = super.fetchParentEntity(client, jsonFormatter, parentURL, homeEntity)
        val homeSirenEntity = homeEntity
        require(homeSirenEntity != null) { HOME_ERR_MESSAGE }

        return ensureRelation(
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
     * @return [Relation] Login url and method
     */
    private suspend fun ensureLoginAction(): Relation {
        homeEntity = super.fetchParentEntity(client, jsonFormatter, parentURL,homeEntity)
        val homeSirenEntity = homeEntity
        require(homeSirenEntity != null) { HOME_ERR_MESSAGE }

        return ensureRelation(
            parentSirenEntity = homeSirenEntity,
            relation = LOGIN_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }
}