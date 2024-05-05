package com.example.nutrifit.pojo

import java.io.Serializable

data class Ticket (
    val usuario : String,
    val tipoTicket : String,
    val comentario : String
): Serializable {}