package com.example.nutrifit.dbUser

import com.example.nutrifit.pojo.User
import com.google.firebase.firestore.FirebaseFirestore


object DatabaseManagerUser {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("usuarios")

    fun getUserByEmail(email: String, onSuccess: (User?) -> Unit, onFailure: (Exception) -> Unit) {
        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(User::class.java)
                    onSuccess(user)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userMap = hashMapOf<String, Any?>(
            "nombre" to user.nombre,
            "apellidos" to user.apellidos,
            "edad" to user.edad,
            "sexo" to user.sexo,
            "altura" to user.altura,
            "peso" to user.peso,
            "nivelActividad" to user.nivelActividad,
            "objetivo" to user.objetivo,
            "calorias" to user.calorias,
            "proteinas" to user.proteinas
        )

        usersCollection.document(user.email)
            .update(userMap)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
