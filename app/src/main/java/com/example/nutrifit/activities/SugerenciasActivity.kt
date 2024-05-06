package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.dbTickets.DatabaseManagerTickets
import com.example.nutrifit.pojo.Ticket
import com.google.firebase.auth.FirebaseAuth
import android.app.AlertDialog

class SugerenciasActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sugerencias)
        val tipoSugerenciaSpinner: Spinner = findViewById(R.id.tipoSugerenciaSpinner)
        val enviarTicket: Button = findViewById(R.id.enviarTicket)
        val comentarioEditText: EditText = findViewById(R.id.cajetinComentario)

        val btnSugerencia: Button = findViewById(R.id.btnSugerencia)
        val btnTusSugerencias: Button = findViewById(R.id.btnTusSugerencias)
        btnSugerencia.setBackgroundResource(R.color.grayOscuro)


        btnTusSugerencias.setOnClickListener {
            val intent = Intent(this, MisSugerenciasActivity::class.java)
            startActivity(intent)
            finish()
        }

        val tipoSugerenciaAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipo_sugerencia,
            R.layout.spinner_ticket_layout
        )
        tipoSugerenciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoSugerenciaSpinner.adapter = tipoSugerenciaAdapter

        tipoSugerenciaSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedFactor = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        val volverButton: Button = findViewById(R.id.volverMainActivityT)
        volverButton.setOnClickListener {
            val intent = Intent(this@SugerenciasActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

        enviarTicket.setOnClickListener {

            val comentario = comentarioEditText.text.toString()
            if (comentario.isNotEmpty()) {

                val tipoTicket = tipoSugerenciaSpinner.selectedItem.toString()


                val usuario = FirebaseAuth.getInstance().currentUser?.email ?: ""


                val ticket = Ticket(usuario, tipoTicket, comentario)


                DatabaseManagerTickets.addTicket(ticket,
                    onSuccess = {

                        val successDialog = AlertDialog.Builder(this)
                            .setTitle("Éxito")
                            .setMessage("El ticket se ha agregado correctamente.")
                            .setPositiveButton("Aceptar") { _, _ ->
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
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

            } else {
                // Mostrar mensaje de error si el campo de comentarios está vacío
                Toast.makeText(this, "El campo de comentarios está vacío", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}