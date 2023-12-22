package com.qtd.modules.auth.service

import com.qtd.exception.BadRequestException
import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom
import kotlin.random.asKotlinRandom

interface IPasswordService {
    fun validatePassword(attempt: String, userPassword: String): Boolean
    fun encryptPassword(password: String): String
    fun generatePasswordWithDefault(): String
    fun validateFeasiblePassword(password: String): Boolean
}

object PasswordService : IPasswordService {
    private val letters = 'a'..'z'
    private val uppercaseLetters = 'A'..'Z'
    private val numbers = '0'..'9'
    private const val specials: String = "@#=+!£\$%&?"

    override fun validatePassword(attempt: String, userPassword: String): Boolean {
        return BCrypt.checkpw(attempt, userPassword)
    }

    override fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun validateFeasiblePassword(password: String): Boolean {
        val hasLetter = password.any { it in letters }
        val hasUppercase = password.any { it in uppercaseLetters }
        val hasNumber = password.any { it in numbers }
        val hasSpecial = password.any { it in specials }

        if (!hasLetter) {
            throw BadRequestException("Password must contain at least one letter")
        }
        if (!hasUppercase) {
            throw BadRequestException("Password must contain at least one uppercase letter")
        }
        if (!hasNumber) {
            throw BadRequestException("Password must contain at least one number")
        }
        if (!hasSpecial) {
            throw BadRequestException("Password must contain at least one special character")
        }
        if (password.length < 6) {
            throw BadRequestException("Password must be at least 6 characters long")
        }
        return true
    }

    override fun generatePasswordWithDefault() = generatePassword()

    fun generatePassword(
        isWithLetters: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int = 6
    ): String {
        var chars = ""

        if (isWithLetters) {
            chars += letters
        }
        if (isWithUppercase) {
            chars += uppercaseLetters
        }
        if (isWithNumbers) {
            chars += numbers
        }
        if (isWithSpecial) {
            chars += specials
        }

        val rnd = SecureRandom.getInstance("SHA1PRNG").asKotlinRandom()
        return List(length) { chars.random(rnd) }.joinToString("")
    }

}