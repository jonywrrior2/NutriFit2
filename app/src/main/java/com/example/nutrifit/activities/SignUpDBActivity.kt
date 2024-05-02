package com.example.nutrifit.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
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
import com.example.nutrifit.databinding.ActivitySignupdbBinding
import com.example.nutrifit.pojo.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpDBActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupdbBinding
    private lateinit var factorActividadSpinner: Spinner
    private lateinit var objetivoSpinner: Spinner
    private lateinit var alturaTxt: EditText
    private lateinit var pesoTxt: EditText
    private lateinit var btnVolver: Button
    private lateinit var btnRegistar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupdbBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtener referencia a los componentes
        factorActividadSpinner = findViewById(R.id.factorActividadSpinner)
        alturaTxt = findViewById(R.id.alturaTxt)
        pesoTxt = findViewById(R.id.pesoTxt)
        btnVolver = findViewById(R.id.volverDP)
        btnRegistar = findViewById(R.id.signupbtn)

        val email = intent.getStringExtra("email")
        val nombre = intent.getStringExtra("nombre")
        val apellidos = intent.getStringExtra("apellidos")
        val contrasenha = intent.getStringExtra("contrasenha")
        val sexo = intent.getStringExtra("sexo")

        // boton para volver a la activity anterior de datos personales
        btnVolver.setOnClickListener {
            val intent = Intent(this@SignUpDBActivity, SignUpDPActivity::class.java)
            startActivity(intent)
        }

        btnRegistar.setOnClickListener {
            // Obtener los datos adicionales del usuario
            val edad = binding.edadTxt.text.toString().toIntOrNull() ?: 0
            val altura = binding.alturaTxt.text.toString().toDoubleOrNull() ?: 0.0
            val peso = binding.pesoTxt.text.toString().toDoubleOrNull() ?: 0.0
            val nivelActividad = binding.factorActividadSpinner.selectedItem.toString()
            val objetivo = binding.objetivoSpinner.selectedItem.toString()

            // Verificar si los campos están completos y dentro de los rangos
            if (email != null && nombre != null && apellidos != null && contrasenha != null && sexo != null) {
                if (altura in 100.0..250.0 ) {
                    if (edad in 10..100) {
                        if (peso in 20.0..200.0) {
                            // Crear el objeto User con todos los datos
                            val user = User(
                                nombre,
                                apellidos,
                                email,
                                edad,
                                sexo,
                                altura,
                                peso,
                                nivelActividad,
                                objetivo,
                                0,
                                0
                            )

                            // Registrar y autenticar al usuario
                            registrarYAutenticarUsuario(email, contrasenha, user)
                        } else {
                            Toast.makeText(
                                this@SignUpDBActivity,
                                "Por favor, introduce un peso válido entre 20 y 200 kg",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@SignUpDBActivity,
                            "Por favor, introduce una edad válida entre 10 y 100 años ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@SignUpDBActivity,
                        "Por favor, introduce una altura válida entre 100 y 250 cm",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@SignUpDBActivity,
                    "Por favor, completa todos los campos correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Crear un ArrayAdapter usando el array de strings definido en strings.xml para Factor de Actividad
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
                    // Aquí puedes hacer algo con el factor de actividad seleccionado
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Manejar la situación en la que no se selecciona nada
                }
            }

        // Obtener referencia al Spinner de Objetivo
        objetivoSpinner = findViewById(R.id.objetivoSpinner)

        // Crear un ArrayAdapter usando el array de strings definido en strings.xml para Objetivo
        val objetivoAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_objetivo,
            R.layout.spinner_item_layout
        )
        objetivoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        objetivoSpinner.adapter = objetivoAdapter

        // Manejar la selección del Spinner de Objetivo
        objetivoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedObjetivo = parent.getItemAtPosition(position).toString()
                // Aquí puedes hacer algo con el objetivo seleccionado
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar la situación en la que no se selecciona nada
            }
        }


    }

    // Función para mostrar el diálogo
    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun registrarYAutenticarUsuario(email: String, contrasenha: String, user: User) {
        // Registrar al usuario en Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, contrasenha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, obtenemos el usuario actual
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (firebaseUser != null) {
                        // Ajustar las calorías y proteínas según el objetivo seleccionado
                        ajustarCaloriasYProteinas(user)

                        val calorias = user.calorias
                        val proteinas = user.proteinas

                        // Guardamos el usuario en Firestore
                        guardarUsuarioEnFirestore(user)

                        // Mostrar mensaje de éxito y redirigir al usuario a la pantalla de inicio de sesión
                        mostrarMensajeExito(calorias, proteinas)

                    } else {
                        Toast.makeText(
                            this@SignUpDBActivity,
                            "Error: usuario actual nulo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Error en el registro
                    Toast.makeText(
                        this@SignUpDBActivity,
                        "Error al registrar el usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun guardarUsuarioEnFirestore(usuario: User) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(usuario.email).set(usuario)
            .addOnSuccessListener {
                Toast.makeText(
                    this@SignUpDBActivity,
                    "Usuario registrado exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
                // Éxito al guardar el usuario en Firestore
                // Después de guardar el usuario en Firestore, puedes redirigir al usuario a otra actividad si es necesario
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@SignUpDBActivity,
                    "Error al registrar el usuario en Firestore: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                // Error al guardar el usuario en Firestore
            }
    }

    // Función para ajustar las calorías y proteínas según el objetivo seleccionado
    private fun ajustarCaloriasYProteinas(user: User) {
        val tmb: Double = if (user.sexo == "Masculino") {
            66.4730 + (13.7516 * user.peso) + (5.0033 * user.altura) - (6.7550 * user.edad)
        } else {
            655.0955 + (9.5634 * user.peso) + (1.8449 * user.altura) - (4.6756 * user.edad)
        }

        val factorActividad = when (user.nivelActividad) {
            "Poco o ningún ejercicio" -> 1.2
            "Ejercicio ligero (1-3 días a la semana)" -> 1.375
            "Ejercicio moderado (3-5 días a la semana)" -> 1.55
            "Ejercicio fuerte (6-7 días a la semana)" -> 1.725
            "Ejercicio muy fuerte (dos veces al día, entrenamientos muy duros)" -> 1.9
            else -> 1.0 // Si no se selecciona un factor de actividad válido, usar 1.0
        }

        var caloriasNecesarias = (tmb * factorActividad).toInt()
        var proteinasNecesarias = 0 // Variable para calcular las proteínas necesarias

        when (user.objetivo) {
            "Bajar Peso" -> {
                caloriasNecesarias -= 500
                proteinasNecesarias = (user.peso * 1.3).toInt()
            }
            "Subir Peso" -> {
                caloriasNecesarias += 700
                proteinasNecesarias = (user.peso * 2.0).toInt()
            }
            "Recomposición Corporal" -> {
                caloriasNecesarias -= 150
                proteinasNecesarias = (user.peso * 2.0).toInt()
            }
            "Mantener Peso" -> {

                proteinasNecesarias = (user.peso * 1.9).toInt()
            }
        }


        println("Proteínas necesarias: ${proteinasNecesarias}")

        // Asignar las calorías y las proteínas necesarias al objeto User
        user.calorias = caloriasNecesarias
        user.proteinas = proteinasNecesarias
    }


    // Función para mostrar el mensaje de éxito y redirigir al usuario a la pantalla de inicio de sesión
    private fun mostrarMensajeExito(calorias: Int, proteinas: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage("Se ha registrado exitosamente.\n\n" +
                "Calorías necesarias: $calorias Kcal\n" +
                "Proteínas necesarias: $proteinas g\n\n" +
                "Por favor, inicie sesión.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@SignUpDBActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        val dialog = builder.create()
        dialog.show()
    }

}
