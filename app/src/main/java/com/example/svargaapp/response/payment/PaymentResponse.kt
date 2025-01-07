package com.example.svargaapp.response.payment

data class PaymentResponse(
    val payment_id: Int,
    val order_id: Int,
    val id_discount: String,
    val id_method: Int,
    val payment_status: String,
    val payment_date: String,
    val total: Int,
    val status: String
)
