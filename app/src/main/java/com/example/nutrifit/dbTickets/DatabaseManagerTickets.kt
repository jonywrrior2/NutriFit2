package com.example.nutrifit.dbTickets

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
    }

