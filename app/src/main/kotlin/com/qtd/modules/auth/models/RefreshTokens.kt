package com.qtd.modules.auth.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object RefreshTokens : UUIDTable(), IRefreshTokenDao {
    val token = varchar("token", 1000).uniqueIndex()
    val userId = uuid("user_id").references(Users.id)
    val expiresAt = datetime("expires_at")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val revoked = bool("revoked").default(false)

    override fun newRefreshToken(userId: UUID, token: String, expiredTime: LocalDateTime) = RefreshToken.new {
        this.userId = userId
        this.token = token
        expiresAt = expiredTime
    }

    override fun revokeAllTokens(userId: UUID) {
        RefreshToken
            .find { RefreshTokens.userId eq userId and (revoked eq false) }
            .forEach { it.revoked = true }
    }
}

class RefreshToken(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshToken>(RefreshTokens)

    var token by RefreshTokens.token
    var userId by RefreshTokens.userId
    var expiresAt by RefreshTokens.expiresAt
    var createdAt by RefreshTokens.createdAt
    var revoked by RefreshTokens.revoked
}

interface IRefreshTokenDao {
    fun newRefreshToken(userId: UUID, token: String, expiredTime: LocalDateTime): RefreshToken
    fun revokeAllTokens(userId: UUID)
}
