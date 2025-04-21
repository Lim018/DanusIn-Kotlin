package com.example.danusin.models

enum class UserRole {
    BUYER, SELLER
}

data class User(
    val email: String,
    val password: String,
    val role: UserRole
)
