package com.example.svargaapp.response.payment

data class PaymentRequest(
    val order_id: Int,
    val id_discount: String,
    val id_method: Int,
    val payment_status: String,
    val payment_date: Long,
    val total: Int,
    val status: String
)