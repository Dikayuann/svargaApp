package com.example.svargaapp.client

import com.example.svargaapp.response.account.LoginResponse
import com.example.svargaapp.response.menu.MenuResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("menu")
    fun getMenu(): Call<ArrayList<MenuResponse>>

    @FormUrlEncoded
    @POST("account")
    fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}