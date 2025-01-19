package com.example.svargaapp.response.order

data class OrderRequest(
    val user_id: Int,
    val total: Double,
    val location: String,
    val order_date: Long,
)
