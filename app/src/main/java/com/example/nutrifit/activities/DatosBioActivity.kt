package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
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
        val txtEdad : TextView = findViewById(R.id.txtEdadUserB)
        val factorActividadSpinner : Spinner = findViewById(R.id.factorActividadSpinnerB)
        val objetivoSpinner : Spinner = findViewById(R.id.objetivoSpinnerB)

        val factorActividadAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.factor_actividad_values,
            R.layout.spinner_item_layout
        )
        factorActividadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        factorActividadSpinner.adapter = factorActividadAdapter

        // Manejar la selección del Spinner de Factor de Actividad
        factorActividadSpinner.onItemSelectedListener =
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


        val objetivoAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_objetivo,
            R.layout.spinner_item_layout
        )
        objetivoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        objetivoSpinner.adapter = objetivoAdapter

        // Manejar la selección del Spinner de Factor de Actividad
        objetivoSpinner.onItemSelectedListener =
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
                        txtEdad.text = it.edad.toString()

                        val factorActividadIndex = obtenerIndiceFactorActividad(it.nivelActividad)
                        factorActividadSpinner.setSelection(factorActividadIndex)

                        val objetivoIndex = obtenerIndiceObjetivo(it.objetivo)
                        objetivoSpinner.setSelection(objetivoIndex)


                    }
                },
                onFailure = { exception ->

                }
            )
        }
    }


    private fun obtenerIndiceFactorActividad(nivelActividad: String): Int {
        val opcionesFactorActividad = resources.getStringArray(R.array.factor_actividad_values)
        return opcionesFactorActividad.indexOf(nivelActividad)
    }

    private fun obtenerIndiceObjetivo(objetivo: String): Int {
        val opcionesObjetivo = resources.getStringArray(R.array.array_objetivo)
        return opcionesObjetivo.indexOf(objetivo)
    }


    fun restarPeso(view: View) {
        val txtPesoUser: TextView = findViewById(R.id.txtPesoUserB)
        var pesoActual = txtPesoUser.text.toString().toDoubleOrNull() ?: 0.0
        if (pesoActual > 20.0) {
            pesoActual -= 0.1
            txtPesoUser.text = String.format("%.1f", pesoActual)
        }
    }

    fun sumarPeso(view: View) {
        val txtPesoUser: TextView = findViewById(R.id.txtPesoUserB)
        var pesoActual = txtPesoUser.text.toString().toDoubleOrNull() ?: 0.0
        if (pesoActual < 200.0) {
            pesoActual += 0.1
            txtPesoUser.text = String.format("%.1f", pesoActual)
        }
    }


    fun restarAltura(view: View) {
        val txtAlturaUser: TextView = findViewById(R.id.txtAlturaUserB)
        var alturaActual = txtAlturaUser.text.toString().toIntOrNull() ?: 0
        if (alturaActual > 100) {
            alturaActual -= 1
            txtAlturaUser.text = alturaActual.toString()
        }
    }

    fun sumarAltura(view: View) {
        val txtAlturaUser: TextView = findViewById(R.id.txtAlturaUserB)
        var alturaActual = txtAlturaUser.text.toString().toIntOrNull() ?: 0
        if (alturaActual < 250) {

            alturaActual += 1
        txtAlturaUser.text = alturaActual.toString()
        }
    }

    fun restarEdad(view: View) {
        val txtEdad: TextView = findViewById(R.id.txtEdadUserB)
        var edadActual = txtEdad.text.toString().toIntOrNull() ?: 0
        if (edadActual > 10) {
            edadActual -= 1
            txtEdad.text = edadActual.toString()
        }
    }

    fun sumarEdad(view: View) {
        val txtEdad: TextView = findViewById(R.id.txtEdadUserB)
        var edadActual = txtEdad.text.toString().toIntOrNull() ?: 0
        if (edadActual < 100) {
            edadActual += 1
        txtEdad.text = edadActual.toString()
        }
    }




}