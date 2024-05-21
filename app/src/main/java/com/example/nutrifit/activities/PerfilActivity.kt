package com.example.nutrifit.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.nutrifit.R
import com.example.nutrifit.databases.DatabaseManagerUser
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

        val txtInfoPersonal : TextView = findViewById(R.id.txtInfoPersonal)
        val txtDatosBiometricos : TextView = findViewById(R.id.txtDatosBio)
        val txtTusCambios : TextView = findViewById(R.id.txtTusCambios)
        val txtAnhadirAlimento: TextView = findViewById(R.id.txtAnhadirAlimentos)
        val viewAddAlimento: View = findViewById(R.id.viewAddAlimento)

        if (currentUserEmail == "cristianbersabe@gmail.com") {
            viewAddAlimento.isVisible = true
            txtAnhadirAlimento.isVisible = true
        }


        txtInfoPersonal.setOnClickListener {
            intent = Intent(this@PerfilActivity, InfoPersonalActivity::class.java)
            intent.putExtra("email", currentUserEmail)
            startActivity(intent)
            finish()
        }

        txtDatosBiometricos.setOnClickListener {
            intent = Intent(this@PerfilActivity, DatosBioActivity::class.java)
            intent.putExtra("email", currentUserEmail)
            startActivity(intent)
            finish()
        }
        txtTusCambios.setOnClickListener {
            intent = Intent(this@PerfilActivity, TusCambiosActivity::class.java)
            intent.putExtra("email", currentUserEmail)
            startActivity(intent)
            finish()
        }

        txtAnhadirAlimento.setOnClickListener {
            intent = Intent(this@PerfilActivity, AnhadirAlimentoActivity::class.java)
            intent.putExtra("email", currentUserEmail)
            startActivity(intent)
            finish()
        }

        currentUserEmail?.let { email ->
            DatabaseManagerUser.getUserByEmail(email,
                onSuccess = { user ->
                    user?.let {
                        txtNombreyApellidos.text = "${it.nombre} ${it.apellidos}"
                    } ?: mostrarAlerta("Usuario no encontrado", "El usuario con correo electrónico $email no fue encontrado en la base de datos.")
                },
                onFailure = { exception ->
                    mostrarAlerta("Error", "Error al consultar la base de datos: ${exception.message}")
                }
            )
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
