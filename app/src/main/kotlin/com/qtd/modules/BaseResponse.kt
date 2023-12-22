package com.qtd.modules

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.baseRespond(response: BaseResponse) {
    respond(response.httpStatus(), response)
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
open class BaseResponse {
    var status: Int = 0

    var message: String? = null

    var data: Any? = null

    var error: Any? = null

    fun httpStatus() = HttpStatusCode.fromValue(status)

    companion object {
        fun created(data: Any? = null): BaseResponse {
            val response = BaseResponse()
            response.status = 201
            response.message = "Created"
            response.data = data
            return response
        }

        fun success(data: Any? = null): BaseResponse {
            val response = BaseResponse()
            response.status = 200
            response.message = "Success"
            response.data = data
            return response
        }

        fun serverError(message: String = "Server error", error: Any? = null): BaseResponse {
            val response = BaseResponse()
            response.status = 500
            response.message = message
            response.error = error
            return response
        }

        fun badRequestError(message: String = "Bad request error", error: Any? = null): BaseResponse {
            val response = BaseResponse()
            response.status = 400
            response.message = message
            response.error = error
            return response
        }

        fun authenticationError(message: String = "Authorization error"): BaseResponse {
            val response = BaseResponse()
            response.status = 401
            response.message = message
            return response
        }

        fun permissionError(message: String = "Permission denied", error: Any? = null): BaseResponse {
            val response = BaseResponse()
            response.status = 403
            response.message = message
            response.error = error
            return response
        }
    }
}