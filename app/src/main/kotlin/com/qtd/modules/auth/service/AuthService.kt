package com.qtd.modules.auth.service

import com.qtd.modules.BaseService
import com.qtd.modules.auth.dto.RegisterUserRequest
import com.qtd.modules.auth.dto.UpdateUserRequest
import com.qtd.modules.auth.dto.UserCredentialsResponse
import com.qtd.modules.auth.model.*
import com.qtd.utils.AuthenticationException
import com.qtd.utils.UserDoesNotExists
import com.qtd.utils.UserExists
import org.koin.core.component.inject
import java.util.*

interface IAuthService {
    suspend fun register(registerUser: RegisterUserRequest): UserCredentialsResponse
    suspend fun login(email: String, password: String): UserCredentialsResponse
    suspend fun refresh(refreshToken: String): UserCredentialsResponse

    suspend fun getUserById(id: String): User
    suspend fun updateUser(id: String, updateUser: UpdateUserRequest): User
    suspend fun getAllUsers(): List<User>
    suspend fun logout(userId: String)
}

class AuthService : BaseService(), IAuthService {
    private val passwordEncryption by inject<IPasswordService>()
    private val tokenProvider by inject<ITokenService>()
    private val refreshTokenDao by inject<IRefreshTokenDao>()
    private val userDao by inject<IUserDao>()

    override suspend fun register(registerUser: RegisterUserRequest): UserCredentialsResponse = dbQuery {
        if (userDao.isExists(registerUser.user.email, registerUser.user.username)) {
            throw UserExists()
        }

        val newUser = userDao.createNewUser(
            email = registerUser.user.email,
            username = registerUser.user.username,
            password = passwordEncryption.encryptPassword(registerUser.user.password)
        )

        val tokens = tokenProvider.createTokens(newUser)
        refreshTokenDao.newRefreshToken(newUser.id.value, tokens.refreshToken, tokens.refreshTokenExpiredTime)

        UserCredentialsResponse.fromUser(newUser, tokens)
    }

    override suspend fun login(email: String, password: String): UserCredentialsResponse = dbQuery {
        val user = User.find { (Users.email eq email) }.firstOrNull() ?: throw UserDoesNotExists()
        if (passwordEncryption.validatePassword(password, user.password)) {

            refreshTokenDao.revokeAllTokens(user.id.value)

            val tokens = tokenProvider.createTokens(user)
            refreshTokenDao.newRefreshToken(user.id.value, tokens.refreshToken, tokens.refreshTokenExpiredTime)

            UserCredentialsResponse.fromUser(user, tokens)
        } else {
            throw AuthenticationException()
        }
    }

    override suspend fun refresh(refreshToken: String): UserCredentialsResponse {
        /**
         * validate refresh token by verifier whether it is expired or not
         * if it is expired, throw AuthenticationException
         * if it is not expired, get the refresh token from the database
         * if it is not found, throw AuthenticationException
         * if it is found, check if the refresh token is revoked or not
         * if it is revoked, throw AuthenticationException
         * if it is not revoked, get the user from the database
         * generate new refreshToken and accessToken
         * revoke all old refreshTokens from the database
         * save the refreshToken to the database
         * return the new refreshToken and accessToken
         */
        tokenProvider.verifyRefreshToken(refreshToken)?.let { userId ->
            dbQuery {
                if (!refreshTokenDao.verifyToken(refreshToken)) {
                    throw AuthenticationException()
                }
            }
            val user = getUserById(userId)
            val tokens = tokenProvider.createTokens(user)
            dbQuery {
                refreshTokenDao.deleteToken(refreshToken)
                refreshTokenDao.newRefreshToken(
                    user.id.value,
                    tokens.refreshToken,
                    tokens.refreshTokenExpiredTime,
                )
            }
            return UserCredentialsResponse.fromUser(user, tokens)
        }

        throw AuthenticationException()

    }


    override suspend fun getUserById(id: String) = dbQuery { getUser(id) }

    override suspend fun updateUser(id: String, updateUser: UpdateUserRequest) = dbQuery {
        getUser(id).apply {
            email = updateUser.user.email ?: email
            password = updateUser.user.password ?: password
            username = updateUser.user.username ?: username
            image = updateUser.user.image ?: image
            bio = updateUser.user.bio ?: bio
        }
    }

    override suspend fun getAllUsers() = dbQuery { User.all().toList() }

    override suspend fun logout(userId: String) {
        dbQuery {
            refreshTokenDao.revokeAllTokens(UUID.fromString(userId))
        }
    }

}

fun getUser(id: String) = User.findById(UUID.fromString(id)) ?: throw UserDoesNotExists()