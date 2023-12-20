package com.qtd.modules.auth.models

import com.qtd.utils.UserExists
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.or
import java.util.*

object Users : UUIDTable(), IUserDao {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val bio = text("bio").default("")
    val image = varchar("image", 255).nullable()
    val password = varchar("password", 255)

    override fun createNewUser(email: String, username: String, password: String) = User.new {
        this.username = username
        this.email = email
        this.password = password
    }


    override fun isExists(email: String?, username: String?): Boolean {
        var isExists = false

        //throw error if both are null
        if (email == null && username == null) {
            throw IllegalArgumentException("email and username cannot be null")
        }
        User.find {
            (if (username != null) {
                Users.username eq username
            } else {
                Op.TRUE
            }) or (if (email != null) {
                Users.email eq email
            } else {
                Op.TRUE
            })
        }.firstOrNull()?.let {
            isExists = true
        }

        return isExists
    }

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

interface IUserDao {
    fun createNewUser(email: String, username: String, password: String): User
    fun isExists(email: String?, username: String?): Boolean
}