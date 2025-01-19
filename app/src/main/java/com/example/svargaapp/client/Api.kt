package com.example.svargaapp.client

import com.example.svargaapp.response.account.LoginResponse
import com.example.svargaapp.response.account.RegisterRequest
import com.example.svargaapp.response.account.RegisterResponse
import com.example.svargaapp.response.account.ResponseData
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.cart.CartRequest
import com.example.svargaapp.response.cart.CartResponse
import com.example.svargaapp.response.discon.DiscountResponse
import com.example.svargaapp.response.history.HistoryResponse
import com.example.svargaapp.response.map.MapResponse
import com.example.svargaapp.response.menu.MenuResponse
import com.example.svargaapp.response.order.OrderRequest
import com.example.svargaapp.response.order.OrderResponse
import com.example.svargaapp.response.payment.PaymentRequest
import com.example.svargaapp.response.payment.PaymentResponse
import com.example.svargaapp.response.paymentMethod.PaymentMethodResponse
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
    @POST("account/login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): Call<RegisterRequest>



    @FormUrlEncoded
    @PUT("account/updateProfile")
    fun updateProfile(
        @Field("user_id") user_id: Int,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("phone_number") phone_number: String,
        @Field("password") password: String,
        @Field("newPassword")newPassword: String
    ): Call<ResponseData>

    @POST("cart/add")
    fun addToCart(@Body cartRequest: CartRequest): Call<CartResponse>

    @GET("cart/items/{user_id}")
    fun getCartItems(
        @Path("user_id") userId: Int
    ): Call<ArrayList<CartItem>>

    @PUT("cart/update/{cartId}")
    fun updateCartItem(
        @Path("cartId") cartId: String,
        @Body CartRequest: CartRequest
    ): Call<CartResponse>


    @DELETE("cart/remove/{userId}/{cartId}")
    fun removeCartItem(
        @Path("userId") userId: Int,
        @Path("cartId") cartId: String
    ): Call<CartResponse>

    @GET("paymentmethod")
    fun getPaymentMethods(): Call<List<PaymentMethodResponse>>

    @GET("discount")
    fun getDiscount(): Call<List<DiscountResponse>>

    @POST("orders")
    fun createOrder(
        @Body order: OrderRequest
    ): Call<OrderResponse>

    @FormUrlEncoded
    @PUT("location/update/{user_id}")
    fun putLocation(
        @Path("user_id") userId: Int,
        @Field("location") newLocation: String
    ): Call<MapResponse>

    @POST("payment/create")
    fun createPayment(@Body paymentRequest: PaymentRequest): Call<PaymentResponse>

    @GET("history/history/{user_id}")
    fun getHistory(
        @Path("user_id") userId: Int
    ): Call<ArrayList<HistoryResponse>>

    @FormUrlEncoded
    @POST("account/register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): Call<RegisterResponse>

}


