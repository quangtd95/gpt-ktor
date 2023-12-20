package com.qtd.modules.profile.model

data class ProfileResponse (val profile: Profile? = null) {
    data class Profile(val username: String, val bio: String, val  image: String?, val following: Boolean = false)
}