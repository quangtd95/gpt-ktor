package com.qtd.modules.auth.service

import com.qtd.modules.BaseService
import com.qtd.modules.auth.dto.RegisterUserRequest
import com.qtd.modules.auth.dto.UpdateUserRequest
import com.qtd.modules.auth.dto.UserCredentialsResponse
import com.qtd.modules.auth.models.*
import com.qtd.utils.AuthenticationException
import com.qtd.utils.UserDoesNotExists
import com.qtd.utils.UserExists
import org.koin.core.component.inject
import java.util.*

interface AuthUseCase {
    suspend fun register(registerUser: RegisterUserRequest): UserCredentialsResponse
    suspend fun login(email: String, password: String): UserCredentialsResponse

    suspend fun getUserById(id: String): User
    suspend fun updateUser(id: String, updateUser: UpdateUserRequest): User
    suspend fun getAllUsers(): List<User>
}

class AuthService : BaseService(), AuthUseCase {
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

}

fun getUser(id: String) = User.findById(UUID.fromString(id)) ?: throw UserDoesNotExists()

fun getUserByUsername(username: String) = User.find { Users.username eq username }.firstOrNull()