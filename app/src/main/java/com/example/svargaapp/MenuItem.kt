package com.example.svargaapp

data class MenuItem(
    val name: String,
    val description: String,
    val price: String,
    val category: String, // Tambahkan kategori
    val image: Int
)
