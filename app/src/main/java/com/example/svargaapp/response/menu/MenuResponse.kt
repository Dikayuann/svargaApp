package com.example.svargaapp.response.menu

data class MenuResponse(
    val idMenu: Int,
    val name: String,
    val description: String,
    val price: Int,
    val category: String, // Tambahkan kategori
    val image: String

)
