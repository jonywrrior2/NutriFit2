package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R

class SugerenciasActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sugerencias)
        val tipoSugerenciaSpinner: Spinner = findViewById(R.id.tipoSugerenciaSpinner)

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

       val volverButton : Button =  findViewById(R.id.volverMainActivityT)
        volverButton.setOnClickListener {
            val intent = Intent(this@SugerenciasActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
        }


    }
}