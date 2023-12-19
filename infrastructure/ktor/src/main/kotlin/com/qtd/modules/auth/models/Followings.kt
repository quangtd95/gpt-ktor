package com.qtd.modules.auth.models

import org.jetbrains.exposed.dao.id.UUIDTable

object Followings : UUIDTable() {
    val userId = reference("userId", Users)
    val followerId = reference("followerId", Users)
}