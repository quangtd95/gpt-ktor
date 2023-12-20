package com.qtd.modules.auth.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object RefreshTokens : UUIDTable() {
    val token = varchar("token", 1000).uniqueIndex()
    val userId = uuid("user_id").references(Users.id)
    val expiresAt = datetime("expires_at")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val revoked = bool("revoked").default(false)
}

class RefreshToken(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshToken>(RefreshTokens)

    var token by RefreshTokens.token
    var userId by RefreshTokens.userId
    var expiresAt by RefreshTokens.expiresAt
    var createdAt by RefreshTokens.createdAt
    var revoked by RefreshTokens.revoked
}