package com.example.nutrifit.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.databases.DatabaseManager
import com.example.nutrifit.databases.DatabaseManagerTickets
import com.example.nutrifit.pojo.Alimento
import com.example.nutrifit.pojo.Ticket
import com.google.firebase.auth.FirebaseAuth

class AnhadirAlimentoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anhadir_alimento)

        val txtNombre : EditText = findViewById(R.id.txtNombreA)
        val txtCantidad : EditText = findViewById(R.id.txtCantidadA)
        val txtCalorias : EditText = findViewById(R.id.txtCaloriasA)
        val txtProteinas : EditText = findViewById(R.id.txtProteinasA)
        val txtUnidad : EditText = findViewById(R.id.txtUnidadUsadaA)
        val buttonAnhadirAlimento : Button = findViewById(R.id.guardarAlimentoA)

        val volverButton : Button = findViewById(R.id.volverPerfilA)

        volverButton.setOnClickListener {
            intent = Intent(this@AnhadirAlimentoActivity, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonAnhadirAlimento.setOnClickListener {

                val nombre = txtNombre.text.toString()
                val cantidad = txtCantidad.text.toString().toDouble()
                val calorias = txtCalorias.text.toString().toDouble()
                val proteinas = txtProteinas.text.toString().toDouble()
                val unidad = txtUnidad.text.toString()



                val alimento = Alimento(nombre, calorias, proteinas,cantidad,unidad)


                DatabaseManager.addAlimento(alimento,
                    onSuccess = {

                        val successDialog = AlertDialog.Builder(this)
                            .setTitle("Éxito")
                            .setMessage("El alimento se ha agregado correctamente.")
                            .setPositiveButton("Aceptar") { _, _ ->
                            }
                            .create()
                        successDialog.show()
                    },
                    onFailure = { exception ->

                        val errorDialog = AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("No se pudo agregar el ticket. Error: ${exception.message}")
                            .setPositiveButton("Aceptar", null)
                            .create()
                        errorDialog.show()
                    }
                )

            }
        }
}