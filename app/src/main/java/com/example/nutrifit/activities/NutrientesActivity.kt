package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.dbMenus.DatabaseManagerMenu
import com.example.nutrifit.pojo.Menu
import com.google.firebase.auth.FirebaseAuth

class NutrientesActivity : AppCompatActivity() {

    private lateinit var guardarAlimentoButton: Button
    private lateinit var volverbutton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrientes)



        val nombre = intent.getStringExtra("nombre")
        var calorias = intent.getDoubleExtra("kcal", 0.0)
        var proteinas = intent.getDoubleExtra("proteinas", 0.0)
        var cantidad = intent.getDoubleExtra("cantidad", 0.0)
        val unidad = intent.getStringExtra("unidad")
        val tipo = intent.getStringExtra("tipo")
        val fecha = intent.getStringExtra("fechaSeleccionada")


        var cantidadSeleccionada = cantidad
        var basec = calorias
        var basep = proteinas

        val txtNombre = findViewById<TextView>(R.id.txtcomidaNutriente)
        val txtUnidad = findViewById<TextView>(R.id.txtUnidadUsada)
        val txtCalorias = findViewById<TextView>(R.id.txtCaloriasAN)
        val txtProteinas = findViewById<TextView>(R.id.txtProteinasAN)
        val pickerCantidad = findViewById<NumberPicker>(R.id.numberPickerCantidad)
        guardarAlimentoButton = findViewById(R.id.guardarAlimento)
        volverbutton = findViewById(R.id.volverComidaActivity)

        volverbutton.setOnClickListener {



            val intent = Intent(this@NutrientesActivity, AnhadirComidaActivity::class.java).apply {
                putExtra("tipo", tipo)
                putExtra("fechaSeleccionada", fecha)
            }

            startActivity(intent)
            finish()

        }

        guardarAlimentoButton.setOnClickListener {

            val comidaNutriente = txtNombre.text.toString()
            val calorias = txtCalorias.text.toString().split(" ")[0].toDouble()
            val proteinas = txtProteinas.text.toString().split(" ")[0].toDouble()
            val usuario = FirebaseAuth.getInstance().currentUser?.email ?: ""
            val fechaStr = fecha ?: ""

            val menu = Menu(comidaNutriente, cantidadSeleccionada.toInt(), calorias, proteinas, unidad!!, usuario, tipo!!, fechaStr)

            DatabaseManagerMenu.addMenu(
                menu,
                onSuccess = {
                    Log.d("DatabaseManagerMenu", "Menu añadido correctamente")
                    Toast.makeText(this, "Menu añadido correctamente", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->

                    Log.e("DatabaseManagerMenu", "Error al añadir el menú", exception)
                    Toast.makeText(this, "Error al añadir el menú: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )


            val intent = Intent(this@NutrientesActivity, AnhadirComidaActivity::class.java).apply {
                putExtra("comidaNutriente", comidaNutriente)
                putExtra("Cantidad", cantidadSeleccionada)
                putExtra("calorias", calorias)
                putExtra("proteinas", proteinas)
                putExtra("unidad", unidad)
                putExtra("tipo", tipo)
                putExtra("fechaSeleccionada", fecha)
            }

            startActivity(intent)
            finish()
        }




        val minValue = 25
        val maxValue = 1000
        val step = 25
        val valueCount = ((maxValue - minValue) / step) + 1
        val displayValues = Array(valueCount) { i -> (minValue + i * step).toString() }
        pickerCantidad.displayedValues = displayValues
        pickerCantidad.minValue = 0
        pickerCantidad.maxValue = valueCount - 1

        val initialValue = (cantidad.toInt() - minValue) / step
        pickerCantidad.value = initialValue

        pickerCantidad.setOnValueChangedListener { _, _, newVal ->
            cantidadSeleccionada = (minValue + newVal * step).toDouble()
            val calorias = (basec * cantidadSeleccionada / 50)
            val proteinas = (basep * cantidadSeleccionada / 50)
            txtCalorias.text = "$calorias kcal"
            txtProteinas.text = "$proteinas g"
            Log.d("NutrientesActivity", "Valor seleccionado: $cantidadSeleccionada")
        }

        pickerCantidad.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        txtNombre.text = nombre
        txtUnidad.text = unidad
        txtCalorias.text = "$calorias / kcal"
        txtProteinas.text = "$proteinas g"
    }




}
