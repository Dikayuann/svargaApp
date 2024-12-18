package com.example.svargaapp.client

import com.example.svargaapp.response.account.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("account")
    fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}