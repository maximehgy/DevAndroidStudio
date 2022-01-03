package com.hema.todo.user

import com.hema.todo.network.Api
import com.hema.todo.tasklist.Task
import okhttp3.MultipartBody

class UserInfoRepository {
    private val UserWebService = Api.userWebService

    suspend fun refresh() : UserInfo? {
        var response = UserWebService.getInfo()
        return if (response.isSuccessful) response.body()!! else null
    }

    suspend fun updateAvatar(avatar : MultipartBody.Part) : UserInfo? {
        val updateAvatarResponse = UserWebService.updateAvatar(avatar)
        return if (updateAvatarResponse.isSuccessful) updateAvatarResponse.body() else null
    }

    suspend fun updateUser(user : UserInfo) : UserInfo ? {
        val updateUserResponse = UserWebService.update(user)
        return if (updateUserResponse.isSuccessful) updateUserResponse.body() else null
    }

    suspend fun login(user: LoginForm) : LoginResponse ?{
        val loginResponse = UserWebService.login(user)
        return if(loginResponse.isSuccessful) loginResponse.body() else null
    }
}