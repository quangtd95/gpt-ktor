package com.qtd.modules.profile.services

import com.qtd.modules.database.IDatabaseProvider
import com.qtd.modules.profile.models.ProfileResponse
import com.qtd.modules.auth.models.User
import com.qtd.modules.auth.service.getUser
import com.qtd.modules.auth.service.getUserByUsername
import com.qtd.utils.UserDoesNotExists
import org.jetbrains.exposed.sql.SizedCollection

interface IProfileService {
    suspend fun getProfile(username: String, currentUserId: String? = null): ProfileResponse
    suspend fun changeFollowStatus(toUsername: String, fromUserId: String, follow: Boolean): ProfileResponse
}

class ProfileService(private val databaseFactory: IDatabaseProvider) : IProfileService {
    override suspend fun getProfile(username: String, currentUserId: String?): ProfileResponse {
        return databaseFactory.dbQuery {
            val toUser = getUserByUsername(username) ?: return@dbQuery getProfileByUser(null, false)
            currentUserId ?: return@dbQuery getProfileByUser(toUser)
            val fromUser = getUser(currentUserId)
            val follows = isFollower(toUser, fromUser)
            getProfileByUser(toUser, follows)
        }
    }

    override suspend fun changeFollowStatus(toUsername: String, fromUserId: String, follow: Boolean): ProfileResponse {
        databaseFactory.dbQuery {
            val toUser = getUserByUsername(toUsername) ?: throw UserDoesNotExists()
            val fromUser = getUser(fromUserId)
            if (follow) {
                addFollower(toUser, fromUser)
            } else {
                removeFollower(toUser, fromUser)
            }
        }
        return getProfile(toUsername, fromUserId)
    }

    private fun addFollower(user: User, newFollower: User) {
        if (!isFollower(user, newFollower)) {
            user.followers = SizedCollection(user.followers + newFollower)
        }
    }

    private fun removeFollower(user: User, newFollower: User) {
        if (isFollower(user, newFollower)) {
            user.followers = SizedCollection(user.followers - newFollower)
        }
    }
}

fun isFollower(user: User, follower: User?) = if (follower != null) user.followers.any { it == follower } else false

fun getProfileByUser(user: User?, following: Boolean = false) =
    ProfileResponse(profile = user?.run { ProfileResponse.Profile(username, bio, image, following) })