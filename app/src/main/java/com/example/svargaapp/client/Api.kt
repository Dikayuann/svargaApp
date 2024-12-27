package com.example.svargaapp.client

import com.example.svargaapp.response.account.LoginResponse
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.cart.CartRequest
import com.example.svargaapp.response.cart.CartResponse
import com.example.svargaapp.response.menu.MenuResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {
    @GET("menu")
    fun getMenu(): Call<ArrayList<MenuResponse>>

    @FormUrlEncoded
    @POST("account")
    fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("cart/add")
    fun addToCart(@Body cartRequest: CartRequest): Call<CartResponse>

    @GET("cart/items/{user_id}")
    fun getCartItems(
        @Path("user_id") userId: String
    ): Call<ArrayList<CartItem>>
}


