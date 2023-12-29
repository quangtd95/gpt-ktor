package com.qtd.modules.profile.service

import com.qtd.common.BaseService
import com.qtd.modules.auth.dto.UpdateUserRequest
import com.qtd.exception.UserDoesNotExistsException
import com.qtd.modules.auth.model.*
import org.koin.core.component.inject
import java.util.*

interface IUserService {
    suspend fun getUserById(id: String): User
    suspend fun updateUser(id: String, updateUser: UpdateUserRequest): User
    suspend fun getAllUsers(): List<User>
}

class UserService : BaseService(), IUserService {

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

fun getUser(id: String) = User.findById(UUID.fromString(id)) ?: throw UserDoesNotExistsException()

fun getUserByUsername(username: String) = User.find { Users.username eq username }.firstOrNull()