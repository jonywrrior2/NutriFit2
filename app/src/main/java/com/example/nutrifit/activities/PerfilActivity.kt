package com.example.nutrifit.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.pojo.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        val volverButton : Button = findViewById(R.id.volverMainActivityP)
        val txtNombreyApellidos : TextView = findViewById(R.id.txtNombrePerfil)

        currentUserEmail?.let { email ->

            firestore.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val user = querySnapshot.documents[0].toObject(User::class.java)
                        user?.let {
                            txtNombreyApellidos.text = "${it.nombre} ${it.apellidos}"
                        }
                    } else {

                        mostrarAlerta("Usuario no encontrado", "El usuario con correo electrónico $email no fue encontrado en la base de datos.")
                    }
                }
                .addOnFailureListener { exception ->

                    mostrarAlerta("Error", "Error al consultar la base de datos: ${exception.message}")
                }
        }

        volverButton.setOnClickListener {
            intent = Intent(this@PerfilActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
