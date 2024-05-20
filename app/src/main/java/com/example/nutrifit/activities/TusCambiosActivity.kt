package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.cambios.CambiosAdapter
import com.example.nutrifit.databases.DatabaseManagerCambios
import com.example.nutrifit.pojo.Cambios

class TusCambiosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tus_cambios)

        val volverButton: Button = findViewById(R.id.volverActivityPerfilC)
        volverButton.setOnClickListener {
            intent = Intent(this@TusCambiosActivity, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCambios)
        recyclerView.layoutManager = LinearLayoutManager(this)


        obtenerCambiosDeUsuarios(
            onSuccess = { cambiosList ->

                val adapter = CambiosAdapter(cambiosList)
                recyclerView.adapter = adapter
            },
            onFailure = { exception ->

                Toast.makeText(this@TusCambiosActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
    //obtener los cambios del usuario
    private fun obtenerCambiosDeUsuarios(onSuccess: (List<Cambios>) -> Unit, onFailure: (Exception) -> Unit) {
        DatabaseManagerCambios.getUserCambios(
            onSuccess = { cambiosList ->
                onSuccess(cambiosList)
            },
            onFailure = { exception ->
                onFailure(exception)
            }
        )
    }

}
