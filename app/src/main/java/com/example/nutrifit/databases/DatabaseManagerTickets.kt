package com.example.nutrifit.databases

import com.example.nutrifit.pojo.Ticket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseManagerTickets {

    private val db = FirebaseFirestore.getInstance()
    private val ticketsCollection = db.collection("tickets")
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email


        fun addTicket(
            ticket: Ticket,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            ticketsCollection.add(ticket)
                .addOnSuccessListener {
                    onSuccess.invoke()
                }
                .addOnFailureListener { exception ->
                    onFailure.invoke(exception)
                }
        }


    fun getUserTickets(
        onSuccess: (List<Ticket>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val email = currentUserEmail
        if (email != null) {
            ticketsCollection.whereEqualTo("usuario", email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val tickets = mutableListOf<Ticket>()
                    for (document in querySnapshot.documents) {
                        val tipoTicket = document.getString("tipoComentario") ?: ""
                        val comentario = document.getString("comentario") ?: ""
                        val usuario = email
                        val ticket = Ticket(usuario, tipoTicket, comentario)
                        tickets.add(ticket)
                    }
                    onSuccess(tickets)
                }
                .addOnFailureListener { e -> onFailure(e) }
        } else {
            onFailure(Exception("User not logged in"))
        }
    }

}

