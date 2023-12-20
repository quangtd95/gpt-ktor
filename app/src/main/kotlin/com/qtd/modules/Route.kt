package com.qtd.modules

import com.qtd.modules.auth.api.auth
import com.qtd.modules.conversation.api.conversation
import com.qtd.modules.profile.api.profile
import com.qtd.modules.profile.api.user
import io.ktor.server.routing.*

fun Routing.api() {
    route("/api") {
        auth()
        user()
        profile()
        conversation()
    }
}