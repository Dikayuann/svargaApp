package com.example.svargaapp.response.menu

data class MenuResponse(
    val menu_id: Int,
    val name: String,
    val subtitle: String,
    val description: String,
    val price: Int,
    val category: String,
    val image: String

)