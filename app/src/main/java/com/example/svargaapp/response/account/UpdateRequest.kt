package com.example.svargaapp.response.account

data class UpdateRequest(
    val username: String,
    val name: String,
    val phone_number: String,
    val password: String
)
