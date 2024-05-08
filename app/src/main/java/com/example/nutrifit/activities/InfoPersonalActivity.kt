package com.example.nutrifit.activities

import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R


class InfoPersonalActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_personal)

        val checkBoxMasculino: CheckBox = findViewById(R.id.checkBoxMasculino)
        val checkBoxFemenino: CheckBox = findViewById(R.id.checkBoxFemenino)
        val txtNombreUser: TextView = findViewById(R.id.txtNombreUserInfo)
        val txtApellidosUser: TextView = findViewById(R.id.txtApellidosUserInfo)
        val txtEmailUser: TextView = findViewById(R.id.txtCorreoUserInfo)


    }


}