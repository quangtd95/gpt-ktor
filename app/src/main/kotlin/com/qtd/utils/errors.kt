package com.qtd.utils

class UserExists : RuntimeException()

class UserDoesNotExists : RuntimeException()

class AuthenticationException : RuntimeException()

open class ValidationException(val params: Map<String, List<String>>) : RuntimeException()