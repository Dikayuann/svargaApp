package com.example.svargaapp.response.account

data class Data (
    val user_id: Int,
    val username: String,
    val name: String,
    val level: String,
    val password: String,
    val location: String,
    val foto_profile: String,
    val phone_number: String
)