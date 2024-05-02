package com.example.nutrifit.pojo


import java.io.Serializable


data class Menu(
    val alimentos: String,
    val cantidad: Int,
    val kcal: Double,
    val proteinas: Double,
    val unidad: String,
    val usuario: String,
    val tipo: String,
    val fecha:  String
) : Serializable {}