package com.qtd.modules.auth.model

import org.jetbrains.exposed.dao.id.UUIDTable

object Followings : UUIDTable() {
    val userId = reference("userId", Users)
    val followerId = reference("followerId", Users)
}