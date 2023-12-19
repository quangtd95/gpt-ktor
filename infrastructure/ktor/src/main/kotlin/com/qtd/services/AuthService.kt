package com.qtd.services

import com.qtd.models.RegisterUser
import com.qtd.models.User
import com.qtd.models.Users
import com.qtd.utils.UserExists
import org.jetbrains.exposed.sql.or

interface IAuthService {
    suspend fun register(registerUser: RegisterUser): User
    suspend fun loginAndGetUser(email: String, password: String): User
}

class AuthService(private val databaseFactory: IDatabaseFactory) : IAuthService {
    override suspend fun register(registerUser: RegisterUser): User {
        return databaseFactory.dbQuery {
            val userInDatabase =
                User.find { (Users.username eq registerUser.user.username) or (Users.email eq registerUser.user.email) }
                    .firstOrNull()
            if (userInDatabase != null) throw UserExists()
            User.new {
                username = registerUser.user.username
                email = registerUser.user.email
                password = registerUser.user.password
            }
        }
    }

    override suspend fun loginAndGetUser(email: String, password: String): User {
        TODO("Not yet implemented")
    }

}