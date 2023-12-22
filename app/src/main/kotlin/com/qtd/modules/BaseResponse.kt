package com.qtd.modules

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.baseRespond(response: BaseResponse) {
    respond(response.httpStatus(), response)
}

open class BaseResponse {
    var status: Int = 0
    var message: String = ""
    var data: Any? = null

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

        fun serverError(message: String = "Server error"): BaseResponse {
            val response = BaseResponse()
            response.status = 500
            response.message = message
            return response
        }

        fun authorizationError(message: String = "Authorization error"): BaseResponse {
            val response = BaseResponse()
            response.status = 401
            response.message = message
            return response
        }
    }
}