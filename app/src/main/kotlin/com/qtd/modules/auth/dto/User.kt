package com.qtd.modules.auth.dto

import com.qtd.modules.auth.model.Credentials

data class RegisterUserRequest(val user: RegisterUserDto) {
    data class RegisterUserDto(val email: String, val username: String, val password: String) {
        init {
            require(email.isNotBlank()) { "email cannot be blank" }
            require(email.contains("@")) { "email must be valid" }

            require(username.isNotBlank()) { "username cannot be blank" }
            require(username.length > 8) { "username must be at least 8 characters" }

            require(password.isNotBlank()) { "password cannot be blank" }
        }
    }
}

data class LoginUserRequest(val user: LoginUserDto) {
    data class LoginUserDto(val email: String, val password: String) {
        init {
            require(email.isNotBlank()) { "email cannot be blank" }
            require(email.contains("@")) { "email must be valid" }

            require(password.isNotBlank()) { "password cannot be blank" }
        }
    }
}

data class UpdateUserRequest(val user: UpdateUserDto) {
    data class UpdateUserDto(
        val email: String? = null,
        val username: String? = null,
        val password: String? = null,
        val image: String? = null,
        val bio: String? = null
    )
}

data class RefreshTokenRequest(val refreshToken: String) {
    init {
        require(refreshToken.isNotBlank()) { "refreshToken cannot be blank" }
    }
}

data class UserResponse(val user: UserDto) {
    data class UserDto(
        val email: String,
        val username: String,
        val bio: String,
        val image: String?,
    )

    companion object {
        fun fromUser(
            user: com.qtd.modules.auth.model.User,
        ): UserResponse = UserResponse(
            UserDto(
                email = user.email, username = user.username, bio = user.bio, image = user.image
            )
        )
    }
}

data class UserCredentialsResponse(val user: UserDto, val credentials: CredentialsDto) {
    data class UserDto(
        val email: String,
        val username: String,
        val bio: String,
        val image: String?,
    )

    data class CredentialsDto(
        val accessToken: String?,
        val accessTokenExpiredTime: String?,
        val refreshToken: String?,
        val refreshTokenExpiredTime: String?,
    )

    companion object {
        fun fromUser(user: com.qtd.modules.auth.model.User, credentials: Credentials) =
            UserCredentialsResponse(
                UserDto(
                    email = user.email, username = user.username, bio = user.bio, image = user.image
                ), CredentialsDto(
                    accessToken = credentials.accessToken,
                    accessTokenExpiredTime = credentials.accessTokenExpiredTime.toString(),
                    refreshToken = credentials.refreshToken,
                    refreshTokenExpiredTime = credentials.refreshTokenExpiredTime.toString()
                )

            )
    }
}