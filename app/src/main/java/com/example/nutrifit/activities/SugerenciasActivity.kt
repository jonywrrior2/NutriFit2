package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R

class SugerenciasActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sugerencias)

       val volverButton : Button =  findViewById(R.id.volverMainActivityT)
        volverButton.setOnClickListener {
            val intent = Intent(this@SugerenciasActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
        }


    }
}