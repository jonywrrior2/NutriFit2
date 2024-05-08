package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.dbUser.DatabaseManagerUser

class DatosBioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_bio)

        val volverButton: Button = findViewById(R.id.volverPerfilActivityB)
        val txtPesoUser: TextView = findViewById(R.id.txtPesoUserB)
        val txtAlturaUser: TextView = findViewById(R.id.txtAlturaUserB)

        volverButton.setOnClickListener {
            intent = Intent(this@DatosBioActivity, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        val currentUserEmail = intent.getStringExtra("email")

        currentUserEmail?.let { email ->
            DatabaseManagerUser.getUserByEmail(email,
                onSuccess = { user ->
                    user?.let {
                        txtPesoUser.text = it.peso.toString()
                        txtAlturaUser.text = it.altura.toString()


                    }
                },
                onFailure = { exception ->

                }
            )
        }
    }


    fun restarPeso(view: View) {
        val txtPesoUser: TextView = findViewById(R.id.txtPesoUserB)
        var pesoActual = txtPesoUser.text.toString().toDoubleOrNull() ?: 0.0
        if (pesoActual > 0.0) {
            pesoActual -= 0.1
            txtPesoUser.text = String.format("%.1f", pesoActual)
        }
    }

    fun sumarPeso(view: View) {
        val txtPesoUser: TextView = findViewById(R.id.txtPesoUserB)
        var pesoActual = txtPesoUser.text.toString().toDoubleOrNull() ?: 0.0
        pesoActual += 0.1
        txtPesoUser.text = String.format("%.1f", pesoActual)
    }


    fun restarAltura(view: View) {
        val txtAlturaUser: TextView = findViewById(R.id.txtAlturaUserB)
        var alturaActual = txtAlturaUser.text.toString().toIntOrNull() ?: 0
        if (alturaActual > 0) {
            alturaActual -= 1
            txtAlturaUser.text = alturaActual.toString()
        }
    }

    fun sumarAltura(view: View) {
        val txtAlturaUser: TextView = findViewById(R.id.txtAlturaUserB)
        var alturaActual = txtAlturaUser.text.toString().toIntOrNull() ?: 0
        if (alturaActual == 0) {
            alturaActual = 1
        } else {
            alturaActual += 1
        }
        txtAlturaUser.text = alturaActual.toString()
    }




}