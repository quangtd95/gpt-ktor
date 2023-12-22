package com.qtd.modules.auth.exception

open class AuthenticationException(val error: String) : RuntimeException()

class AccessTokenInvalidException : AuthenticationException("Access token is invalid")

class RefreshTokenInvalidException : AuthenticationException("Refresh token is invalid")

class LoginCredentialsInvalidException : AuthenticationException("Login credentials are invalid")