package com.qtd.services

import com.qtd.models.ProfileResponse
import com.qtd.models.User

interface IProfileService {
    suspend fun getProfile(username: String, currentUserId: String? = null): ProfileResponse
}

class ProfileService(private val databaseFactory: IDatabaseFactory) : IProfileService {
    override suspend fun getProfile(username: String, currentUserId: String?): ProfileResponse {
        return databaseFactory.dbQuery {
            val toUser = getUserByUsername(username) ?: return@dbQuery getProfileByUser(null, false)
            currentUserId ?: return@dbQuery getProfileByUser(toUser)
            val fromUser = getUser(currentUserId)
            val follows = isFollower(toUser, fromUser)
            getProfileByUser(toUser, follows)
        }
    }
}

fun isFollower(user: User, follower: User?) = if (follower != null) user.followers.any { it == follower } else false

fun getProfileByUser(user: User?, following: Boolean = false) =
    ProfileResponse(profile = user?.run { ProfileResponse.Profile(username, bio, image, following) })