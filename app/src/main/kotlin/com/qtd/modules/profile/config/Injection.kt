package com.qtd.modules.profile.config

import com.qtd.modules.profile.service.IProfileService
import com.qtd.modules.profile.service.IUserService
import com.qtd.modules.profile.service.ProfileService
import com.qtd.modules.profile.service.UserService
import org.koin.dsl.module

val profileKoinModule = module {

    single<IProfileService> { ProfileService() }
    single<IUserService> { UserService() }
}