package com.example.nutrifit.activities

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.nutrifit.R
import com.google.firebase.auth.FirebaseAuth



class RecuperarPassActivity : AppCompatActivity() {

    // Declaración
    private lateinit var btnReset: Button
    private lateinit var btnBack: Button
    private lateinit var edtEmail: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var strEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperarpass)

        // Inicialización
        btnBack = findViewById(R.id.btnForgotPasswordBack)
        btnReset = findViewById(R.id.btnReset)
        edtEmail = findViewById(R.id.edtForgotPasswordEmail)
        progressBar = findViewById(R.id.forgetPasswordProgressbar)

        mAuth = FirebaseAuth.getInstance()

        // Listener del botón Reset
        btnReset.setOnClickListener {
            strEmail = edtEmail.text.toString().trim()
            if (!TextUtils.isEmpty(strEmail)) {
                resetPassword()
            } else {
                edtEmail.setError("Debe introducir un email")
            }
        }

        // Código del botón Back
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun resetPassword() {
        progressBar.visibility = View.VISIBLE
        btnReset.visibility = View.INVISIBLE

        mAuth.sendPasswordResetEmail(strEmail)
            .addOnSuccessListener {
                Toast.makeText(this@RecuperarPassActivity, "El enlace para restablecer la contraseña ha sido enviado a su correo electrónico", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RecuperarPassActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@RecuperarPassActivity, "Error :- " + e.message, Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                btnReset.visibility = View.VISIBLE
            }
    }
}
