package com.example.svargaapp.response.account

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val `data`: Data
)
