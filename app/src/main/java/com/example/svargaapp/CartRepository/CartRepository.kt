package com.example.svargaapp.CartRepository

import com.example.svargaapp.Product

object CartRepository {

    // Daftar produk yang dipilih (keranjang belanja)
    private val cartItems = mutableListOf<Product>()

    // Mendapatkan produk di keranjang sebagai MutableList untuk memungkinkan modifikasi
    fun getCartItems(): MutableList<Product> = cartItems

    // Menambahkan produk ke keranjang
    fun addToCart(product: Product) {
        val existingProduct = cartItems.find { it.name == product.name }
        if (existingProduct != null) {
            existingProduct.quantity += product.quantity
        } else {
            cartItems.add(product)
        }
    }

    // Menghapus produk dari keranjang
    fun removeFromCart(product: Product) {
        cartItems.remove(product)
    }

    // Membersihkan semua produk di keranjang
    fun clearCart() {
        cartItems.clear()
    }
}
