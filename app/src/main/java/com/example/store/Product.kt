package com.example.store

data class Product(
    var image: Int,
    var name: String,
    var price: Double,
    var inCart: Boolean = false
)
