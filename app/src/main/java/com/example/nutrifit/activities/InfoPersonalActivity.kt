package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.dbUser.DatabaseManagerUser
import com.example.nutrifit.pojo.User

class InfoPersonalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_personal)

        val checkBoxMasculino: CheckBox = findViewById(R.id.checkBoxMasculino)
        val checkBoxFemenino: CheckBox = findViewById(R.id.checkBoxFemenino)
        val txtNombreUser: TextView = findViewById(R.id.txtNombreUserInfo)
        val txtApellidosUser: TextView = findViewById(R.id.txtApellidosUserInfo)
        val txtEmailUser: TextView = findViewById(R.id.txtCorreoUserInfo)
        val volverButton: Button = findViewById(R.id.volverPerfilActivityP)

        volverButton.setOnClickListener {
            intent = Intent(this@InfoPersonalActivity, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }


        val currentUserEmail = intent.getStringExtra("email")

        currentUserEmail?.let { email ->
            DatabaseManagerUser.getUserByEmail(email,
                onSuccess = { user ->
                    user?.let {
                        txtNombreUser.text = it.nombre
                        txtApellidosUser.text = it.apellidos
                        txtEmailUser.text = it.email
                        if (it.sexo == "Masculino") {
                            checkBoxMasculino.isChecked = true
                        } else if (it.sexo == "Femenino") {
                            checkBoxFemenino.isChecked = true
                        }
                    }
                },
                onFailure = { exception ->

                }
            )
        }
    }
}
