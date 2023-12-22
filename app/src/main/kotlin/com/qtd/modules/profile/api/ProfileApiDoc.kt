package com.qtd.modules.profile.api

import com.qtd.config.ApiDoc
import com.qtd.config.SWAGGER_SECURITY_SCHEMA
import com.qtd.config.TAG_PROFILE
import com.qtd.modules.BaseResponse
import com.qtd.modules.profile.model.ProfileResponse
import io.ktor.http.*

val profilesDoc: ApiDoc = {
    tags = listOf(TAG_PROFILE)
}

val getProfileDoc: ApiDoc = {
    description = "Get profile of user"
    request {
        pathParameter<String>("username")
    }
    response {
        HttpStatusCode.OK to {
            class GetProfileResponseType : BaseResponse<ProfileResponse>()
            body(GetProfileResponseType::class)
        }
    }
}

val followDoc : ApiDoc = {
    securitySchemeName = SWAGGER_SECURITY_SCHEMA
}

val postFollowProfileDoc: ApiDoc = {
    description = "Follow profile of user"
    request {
        pathParameter<String>("username")
    }
    response {
        HttpStatusCode.OK to {
            class PostFollowProfileResponseType : BaseResponse<ProfileResponse>()
            body(PostFollowProfileResponseType::class)
        }
    }
}

val deleteFollowProfileDoc : ApiDoc = {
    description = "Unfollow profile of user"
    request {
        pathParameter<String>("username")
    }
    response {
        HttpStatusCode.OK to {
            class DeleteFollowProfileResponseType : BaseResponse<ProfileResponse>()
            body(DeleteFollowProfileResponseType::class)
        }
    }
}