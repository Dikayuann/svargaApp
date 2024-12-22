package com.example.svargaapp.response.menu

data class MenuResponse(
    val idMenu: Int,
    val name: String,
    val subtitle: String,
    val description: String,
    val price: Int,
    val category: String,
    val image: String

)