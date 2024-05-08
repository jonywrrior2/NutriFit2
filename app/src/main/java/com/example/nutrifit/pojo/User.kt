package com.example.nutrifit.pojo

import java.io.Serializable

data class User(
        val nombre: String,
        val apellidos: String,
        val email: String,
        val edad: Int,
        val sexo: String,
        val altura: Int,
        val peso: Double,
        val nivelActividad: String,
        val objetivo: String,
        var calorias: Int,
        var proteinas: Int
    ): Serializable {
        constructor() : this("", "", "", 0, "", 0, 0.0, "", "", 0, 0)
    }


