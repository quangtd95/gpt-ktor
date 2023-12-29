package com.qtd.modules.profile.api

import com.qtd.config.ApiDoc
import com.qtd.config.SWAGGER_SECURITY_SCHEMA
import com.qtd.config.TAG_USER
import com.qtd.common.BaseResponse
import com.qtd.modules.auth.dto.UpdateUserRequest
import com.qtd.modules.auth.dto.UserResponse
import io.ktor.http.*

val usersDoc: ApiDoc = {
    tags = listOf(TAG_USER)
    securitySchemeName = SWAGGER_SECURITY_SCHEMA
}

val getListUsersDoc: ApiDoc = {
    description = "Get list users"
    response {
        HttpStatusCode.OK to {
            class GetListUsersDocType : BaseResponse<List<UserResponse>>()
            body(GetListUsersDocType::class)
        }
    }
}

val getCurrentUserProfileDoc: ApiDoc = {
    description = "Get current user profile"
    response {
        HttpStatusCode.OK to {
            class GetCurrentUserProfileDocType : BaseResponse<UserResponse>()
            body(GetCurrentUserProfileDocType::class)
        }
    }
}

val updateCurrentUserProfileDoc: ApiDoc = {
    description = "Update current user profile"
    request {
        body(UpdateUserRequest::class)
    }
    response {
        HttpStatusCode.OK to {
            class UpdateCurrentUserProfileDocType : BaseResponse<UserResponse>()
            body(UpdateCurrentUserProfileDocType::class)
        }
    }
}