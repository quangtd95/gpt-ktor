package com.qtd.modules.auth.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Users : UUIDTable() {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val bio = text("bio").default("")
    val image = varchar("image", 255).nullable()
    val password = varchar("password", 255)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    var email by Users.email
    var username by Users.username
    var bio by Users.bio
    var image by Users.image
    var password by Users.password
    var followers by User.via(Followings.userId, Followings.followerId)
}