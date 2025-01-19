package com.example.svargaapp.response.account

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: RegisterData?
)


