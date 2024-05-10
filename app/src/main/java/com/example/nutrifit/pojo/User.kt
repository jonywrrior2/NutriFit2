package com.example.nutrifit.pojo

import java.io.Serializable

data class User(
    val nombre: String,
    val apellidos: String,
    val email: String,
    var edad: Int,
    val sexo: String,
    var altura: Int,
    var peso: Double,
    var nivelActividad: String,
    var objetivo: String,
    var calorias: Int,
    var proteinas: Int
    ): Serializable {
        constructor() : this("", "", "", 0, "", 0, 0.0, "", "", 0, 0)
    }


