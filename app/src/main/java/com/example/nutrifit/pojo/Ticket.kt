package com.example.nutrifit.pojo

import java.io.Serializable

data class Ticket (
    val usuario : String,
    val tipoComentario : String,
    val comentario : String
): Serializable {}