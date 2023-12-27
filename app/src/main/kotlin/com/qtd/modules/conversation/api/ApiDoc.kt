package com.qtd.modules.conversation.api

import com.qtd.config.ApiDoc
import com.qtd.config.SWAGGER_SECURITY_SCHEMA
import com.qtd.config.TAG_CONVERSATION
import com.qtd.modules.BaseResponse
import com.qtd.modules.conversation.dto.ConversationMessageResponse
import com.qtd.modules.conversation.dto.ConversationResponse
import com.qtd.modules.conversation.dto.PostChat
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

val conversationsDocs: ApiDoc = {
    tags = listOf(TAG_CONVERSATION)
    securitySchemeName = SWAGGER_SECURITY_SCHEMA
}

val createConversationDoc: ApiDoc = {
    description = "Create conversation"
    response {
        HttpStatusCode.Created to {
            class CreateConversationDocType : BaseResponse<ConversationResponse>()
            body(CreateConversationDocType::class)
        }
    }
}

val getListConversationsDoc: ApiDoc = {
    description = "Get conversations"
    response {
        HttpStatusCode.OK to {
            class GetListConversationsDocType : BaseResponse<List<ConversationResponse>>()
            body(GetListConversationsDocType::class)
        }
    }
}

val deleteAllConversationsDoc: ApiDoc = {
    description = "Delete conversations"
    response {
        HttpStatusCode.OK to {
            class DeleteAllConversationsDocType : BaseResponse<Any>()
            body(DeleteAllConversationsDocType::class)
        }
    }
}

val deleteSingleConversationDoc: ApiDoc = {
    description = "Delete single conversation"
    request {
        pathParameter<String>("conversationId")
    }
    response {
        HttpStatusCode.OK to {
            class DeleteSingleConversationDocType : BaseResponse<Any>()
            body(DeleteSingleConversationDocType::class)
        }
    }
}

val getAllMessagesOfConversationDoc: ApiDoc = {
    description = "Get all messages of conversation"
    request {
        pathParameter<String>("conversationId")
    }
    response {
        HttpStatusCode.OK to {
            class GetAllMessagesOfConversationDocType : BaseResponse<List<ConversationMessageResponse>>()
            body(GetAllMessagesOfConversationDocType::class)
        }
    }
}

val sendNewMessageDoc: ApiDoc = {
    description = "Send new message"
    request {
        pathParameter<String>("conversationId")
        body(PostChat::class)
    }
    response {
        HttpStatusCode.OK to {
            class SendNewMessageDocType : BaseResponse<List<ConversationMessageResponse>>()
            body(SendNewMessageDocType::class)
        }
    }
}

val sendNewMessageStreamDoc: ApiDoc = {
    description = "Send new message stream"
    request {
        pathParameter<String>("conversationId")
        body(PostChat::class)
    }
    response {
        HttpStatusCode.OK to {
            class SendNewMessageStreamDocType : BaseResponse<Flow<String>>()
            body(SendNewMessageStreamDocType::class)
        }
    }
}

val deleteSingleMessageDoc: ApiDoc = {
    description = "Delete single message"
    request {
        pathParameter<String>("conversationId")
        pathParameter<String>("messageId")
    }
    response {
        HttpStatusCode.OK to {
            class DeleteSingleMessageDocType : BaseResponse<Any>()
            body(DeleteSingleMessageDocType::class)
        }
    }
}