package com.example.nutrifit.pojo

import java.io.Serializable

data class Cambios (

    val usuario : String,
    val fecha : String,
    val peso : Double
): Serializable {}