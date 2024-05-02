package com.example.nutrifit.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.databinding.ActivitySignupdpBinding
import com.example.nutrifit.pojo.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class SignUpDPActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupdpBinding
    private lateinit var sexoSpinner: Spinner
    private lateinit var btnVolver: Button
    private lateinit var btnContinuar: Button
    private lateinit var contrasenha: EditText

    private val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@gmail.com$"
    private val NAME_PATTERN = "^[a-zA-Z ]{2,}\$"

    private val PASSWORD_PATTERN = "^(?=.*[A-Z]).{6,}\$"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupdpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //referencia componentes
        btnVolver = findViewById(R.id.volverLG)
        sexoSpinner = findViewById(R.id.sexoSpinner)
        btnContinuar = findViewById(R.id.continuarDB)
        contrasenha = findViewById(R.id.password)

        //evento para volver a la activity de login
        btnVolver.setOnClickListener {
            val intent = Intent(this@SignUpDPActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnContinuar.setOnClickListener {
            val email = binding.email.text.toString()
            val nombre = binding.nombre.text.toString()
            val apellidos = binding.apellidos.text.toString()
            val contrasenha = binding.password.text.toString()
            val sexo = binding.sexoSpinner.selectedItem.toString()

            if (validateName(nombre) && validateName(apellidos) && validateEmail(email) && validatePassword(contrasenha)) {
                val intent = Intent(this@SignUpDPActivity, SignUpDBActivity::class.java)
                // Pasar los datos a la siguiente actividad
                intent.putExtra("email", email)
                intent.putExtra("nombre", nombre)
                intent.putExtra("apellidos", apellidos)
                intent.putExtra("contrasenha", contrasenha)
                intent.putExtra("sexo", sexo)
                startActivity(intent)
            }
        }

        val factorActividadAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_sexo,
            R.layout.spinner_item_layout
        )
        factorActividadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexoSpinner.adapter = factorActividadAdapter

        // Manejar la selección del Spinner de Factor de Actividad
        sexoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedFactor = parent.getItemAtPosition(position).toString()
                // Aquí puedes hacer algo con el factor de actividad seleccionado
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar la situación en la que no se selecciona nada
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (!Pattern.matches(EMAIL_PATTERN, email)) {
            Toast.makeText(this@SignUpDPActivity, "Correo no válido. Debe ser de formato example@gmail.com", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun validateName(name: String): Boolean {
        return if (!Pattern.matches(NAME_PATTERN, name)) {
            Toast.makeText(this@SignUpDPActivity, "Nombre o apellido no válido (deben contener más de dos letras)", Toast.LENGTH_SHORT).show()
            false
        } else true
    }


    private fun validatePassword(password: String): Boolean {
        return if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            Toast.makeText(this@SignUpDPActivity, "La contraseña debe tener al menos 6 caracteres y una letra mayúscula", Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}
