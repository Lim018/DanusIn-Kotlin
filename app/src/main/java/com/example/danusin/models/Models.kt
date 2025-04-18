package com.example.danusin.models

data class Category(
    val id: Int,
    val name: Int,
    val icon: Int
)

data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val image: Int,
    val rating: Float,
    val deliveryTime: Int,
    val sold: Int,
    val isFavorite: Boolean = false,
    val calories: Int? = null,
    val description: String? = null
)
