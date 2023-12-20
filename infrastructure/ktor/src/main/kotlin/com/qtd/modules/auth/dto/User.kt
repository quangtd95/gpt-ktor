package com.qtd.modules.auth.dto

data class RegisterUser(val user: User) {
    data class User(val email: String, val username: String, val password: String)
}

data class LoginUser(val user: User) {
    data class User(val email: String, val password: String)
}

data class UpdateUser(val user: User) {
    data class User(
        val email: String? = null,
        val username: String? = null,
        val password: String? = null,
        val image: String? = null,
        val bio: String? = null
    )
}

data class UserResponse(val user: User) {
    data class User(
        val email: String,
        val username: String,
        val bio: String,
        val image: String?,
    )

    companion object {
        fun fromUser(
            user: com.qtd.modules.auth.models.User,
        ): UserResponse = UserResponse(
            User(
                email = user.email,
                username = user.username,
                bio = user.bio,
                image = user.image
            )
        )
    }
}

data class UserCredentialsResponse(val user: User) {
    data class User(
        val email: String,
        val username: String,
        val bio: String,
        val image: String?,
        val accessToken: String?,
        val refreshToken: String?,
    )

    companion object {
        fun fromUser(user: com.qtd.modules.auth.models.User, credentials: Credentials? = null) =
            UserCredentialsResponse(
                User(
                    email = user.email,
                    accessToken = credentials?.accessToken,
                    refreshToken = credentials?.refreshToken,
                    username = user.username,
                    bio = user.bio,
                    image = user.image
                )
            )
    }
}