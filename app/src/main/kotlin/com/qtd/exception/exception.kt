package com.qtd.exception

open class AuthenticationException(message: String) : RuntimeException(message)
open class BadRequestException(message: String = "Bad request", val error: Any? = null) :
    IllegalArgumentException(message)


class AccessTokenInvalidException : AuthenticationException("Access token is invalid")
class RefreshTokenInvalidException : AuthenticationException("Refresh token is invalid")
class LoginCredentialsInvalidException : AuthenticationException("Login credentials are invalid")

class UserDoesNotExistsException(error: Any? = null) : BadRequestException("User does not exists", error)
class UserExistsException(error: Any? = null) : BadRequestException("User already exists", error)

class PermissionException(message: String = "Permission denied") : RuntimeException(message)

