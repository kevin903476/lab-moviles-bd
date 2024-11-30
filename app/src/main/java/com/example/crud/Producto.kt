package com.example.crud

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val idProveedor: Int
)