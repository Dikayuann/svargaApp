package com.example.svargaapp.response.history

data class HistoryResponse(
    val order_id: String,
    val order_date: String,
    val status: String,
    val total: String,
    val name: String,
    val image: String,
    val additional_items: String
)
