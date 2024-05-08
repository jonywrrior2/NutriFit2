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
}
