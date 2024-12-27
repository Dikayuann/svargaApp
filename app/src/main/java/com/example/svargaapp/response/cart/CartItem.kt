package com.example.svargaapp.response.cart

data class CartItem(
    val cart_id: String,
    val menu_id: String,
    var quantity: Int,
    val added_at: String,
    val name: String,
    val price: String,
    val image: String
)


