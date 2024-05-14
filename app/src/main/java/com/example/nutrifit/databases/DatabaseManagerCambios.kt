package com.example.nutrifit.databases

import com.example.nutrifit.pojo.Cambios
import com.example.nutrifit.pojo.Ticket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseManagerCambios {

    private val db = FirebaseFirestore.getInstance()
    private val cambiosCollection = db.collection("cambios")
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    fun addCambios(
        cambios: Cambios,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        DatabaseManagerCambios.cambiosCollection.add(cambios)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }


    fun getUserTickets(
        onSuccess: (List<Cambios>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val email = DatabaseManagerCambios.currentUserEmail
        if (email != null) {
            DatabaseManagerCambios.cambiosCollection.whereEqualTo("usuario", email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val cambios = mutableListOf<Cambios>()
                    for (document in querySnapshot.documents) {
                        val fecha = document.getString("fecha") ?: ""
                        val peso = document.getDouble("peso") ?: 0.0
                        val usuario = email
                        val cambio = Cambios(usuario, fecha, peso)
                        cambios.add(cambio)
                    }
                    onSuccess(cambios)
                }
                .addOnFailureListener { e -> onFailure(e) }
        } else {
            onFailure(Exception("User not logged in"))
        }
    }

}