package com.example.svargaapp.client

import com.example.svargaapp.response.account.LoginResponse
import com.example.svargaapp.response.account.ResponseData
import com.example.svargaapp.response.account.UpdateRequest
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.cart.CartRequest
import com.example.svargaapp.response.cart.CartResponse
import com.example.svargaapp.response.menu.MenuResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("menu")
    fun getMenu(): Call<ArrayList<MenuResponse>>

    @FormUrlEncoded
    @POST("account")
    fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @FormUrlEncoded
    @PUT("account")
    fun updateProfile(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("phone_number") phone_number: String,
        @Field("password") password: String
    ): Call<ResponseData>

    @POST("cart/add")
    fun addToCart(@Body cartRequest: CartRequest): Call<CartResponse>

    @GET("cart/items/{user_id}")
    fun getCartItems(
        @Path("user_id") userId: String
    ): Call<ArrayList<CartItem>>

    @PUT("cart/update/{cartId}")
    fun updateCartItem(
        @Path("cartId") cartId: String,
        @Body CartRequest: CartRequest
    ): Call<CartResponse>


    @DELETE("cart/remove/{userId}/{cartId}")
    fun removeCartItem(
        @Path("userId") userId: String,
        @Path("cartId") cartId: String
    ): Call<CartResponse>


}


