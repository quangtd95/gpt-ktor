package com.qtd.exception

open class AuthenticationException(message: String) : RuntimeException(message)
open class WrongRequestException(message: String = "Bad request", val data: Any? = null) :
    IllegalArgumentException(message)


class AccessTokenInvalidException : AuthenticationException("Access token is invalid")
class RefreshTokenInvalidException : AuthenticationException("Refresh token is invalid")
class LoginCredentialsInvalidException : AuthenticationException("Login credentials are invalid")

class UserDoesNotExistsException(data: Any? = null) : WrongRequestException("User does not exists", data)
class UserExistsException(data: Any? = null) : WrongRequestException("User already exists", data)

class PermissionException(message: String = "Permission denied") : RuntimeException(message)

