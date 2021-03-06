package com.hema.todo.network

import com.hema.todo.user.LoginForm
import com.hema.todo.user.LoginResponse
import com.hema.todo.user.SignupForm
import com.hema.todo.user.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {

    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>
    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>
    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>
    @POST("users/sign_up")
    suspend fun signup(@Body user: SignupForm): Response<LoginResponse>

}