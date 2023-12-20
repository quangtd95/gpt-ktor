package com.qtd.modules.auth.service

import com.qtd.modules.BaseService
import com.qtd.modules.auth.dto.RegisterUser
import com.qtd.modules.auth.dto.UpdateUser
import com.qtd.modules.auth.dto.UserCredentialsResponse
import com.qtd.modules.auth.models.*
import com.qtd.utils.AuthenticationException
import com.qtd.utils.UserDoesNotExists
import com.qtd.utils.UserExists
import org.jetbrains.exposed.sql.or
import org.koin.core.component.inject
import java.util.*

interface AuthUseCase {
    suspend fun register(registerUser: RegisterUser): UserCredentialsResponse
    suspend fun loginAndGetUser(email: String, password: String): UserCredentialsResponse

    suspend fun getUserById(id: String): User
    suspend fun updateUser(id: String, updateUser: UpdateUser): User
    suspend fun getAllUsers(): List<User>
}

class AuthService : BaseService(), AuthUseCase {
    private val passwordEncryption by inject<IPasswordService>()
    private val tokenProvider by inject<ITokenService>()

    override suspend fun register(registerUser: RegisterUser): UserCredentialsResponse = dbQuery {
        User.find { (Users.username eq registerUser.user.username) or (Users.email eq registerUser.user.email) }
            .firstOrNull()?.let {
                throw UserExists()
            }

        val newUser = User.new {
            username = registerUser.user.username
            email = registerUser.user.email
            password = passwordEncryption.encryptPassword(registerUser.user.password)
        }
        val tokens = tokenProvider.createTokens(newUser)

        RefreshToken.new {
            userId = newUser.id.value
            token = tokens.refreshToken
            expiresAt = tokens.refreshTokenExpiredTime
        }
        UserCredentialsResponse.fromUser(newUser, tokens)
    }

    override suspend fun loginAndGetUser(email: String, password: String): UserCredentialsResponse = dbQuery {
        val user = User.find { (Users.email eq email) }.firstOrNull() ?: throw UserDoesNotExists()
        if (passwordEncryption.validatePassword(password, user.password)) {

            RefreshToken.find {
                RefreshTokens.userId eq user.id.value
            }.forEach {
                it.revoked = true
            }

            val tokens = tokenProvider.createTokens(user)
            RefreshToken.new {
                userId = user.id.value
                token = tokens.refreshToken
                expiresAt = tokens.refreshTokenExpiredTime
            }
            UserCredentialsResponse.fromUser(user, tokens)
        } else {
            throw AuthenticationException()
        }
    }


    override suspend fun getUserById(id: String) = dbQuery { getUser(id) }

    override suspend fun updateUser(id: String, updateUser: UpdateUser) = dbQuery {
        getUser(id).apply {
            email = updateUser.user.email ?: email
            password = updateUser.user.password ?: password
            username = updateUser.user.username ?: username
            image = updateUser.user.image ?: image
            bio = updateUser.user.bio ?: bio
        }
    }

    override suspend fun getAllUsers() = dbQuery { User.all().toList() }

}

fun getUser(id: String) = User.findById(UUID.fromString(id)) ?: throw UserDoesNotExists()

fun getUserByUsername(username: String) = User.find { Users.username eq username }.firstOrNull()