package com.example.svargaapp.response.cart

data class CartRequest(
    val user_id: String,
    val menu_id: String,
    val quantity: Int
)
