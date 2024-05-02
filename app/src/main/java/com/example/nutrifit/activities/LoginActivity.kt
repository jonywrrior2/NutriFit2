package com.example.nutrifit.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.nutrifit.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var emailText: EditText
    private lateinit var passText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView
    private lateinit var swRecordar: SwitchCompat
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var passOlvidada: TextView

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        // Referenciar componentes
        emailText = findViewById(R.id.emailText)
        passText = findViewById(R.id.passText)
        loginButton = findViewById(R.id.btnLogin)
        registerText = findViewById(R.id.registerText)
        swRecordar = findViewById(R.id.swRecordar)
        passOlvidada = findViewById(R.id.passOlvidada)

        // Verificar si el Switch está activado en SharedPreferences
        val recordar = sharedPreferences.getBoolean("recordar", false)
        swRecordar.isChecked = recordar

        // Rellenar automáticamente el correo y la contraseña si se recuerda
        if (recordar) {
            val correoGuardado = sharedPreferences.getString("correo", "")
            val contrasenaGuardada = sharedPreferences.getString("contrasena", "")

            emailText.setText(correoGuardado)
            passText.setText(contrasenaGuardada)
        }

        // Evento para ir a la actividad de registro
        registerText.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpDPActivity::class.java)
            startActivity(intent)
        }

        passOlvidada.setOnClickListener {
            val intent = Intent(this@LoginActivity, RecuperarPassActivity::class.java)
            startActivity(intent)
        }

        // Evento de clic en el botón de inicio de sesión
        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Autenticar al usuario con Firebase Auth
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Guardar el correo y la contraseña en SharedPreferences si se recuerda
                            if (swRecordar.isChecked) {
                                sharedPreferences.edit().putString("correo", email).apply()
                                sharedPreferences.edit().putString("contrasena", password).apply()
                            }

                            // Redirigir a la actividad principal
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            // Error al iniciar sesión
                            showDialog("Error", "El email o la contraseña no son correctos.")
                        }
                    }
            } else {
                // Mostrar mensaje de error si los campos están vacíos
                showDialog("Error", "Por favor, completa todos los campos.")
            }
        }

        // Establecer un listener para el SwitchCompat
        swRecordar.setOnCheckedChangeListener { _, isChecked ->
            // Guardar el estado del Switch en SharedPreferences
            sharedPreferences.edit().putBoolean("recordar", isChecked).apply()
        }
    }

    fun signInWithGoogle(view: View) {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("300253647060-lv6s2g5lpmbqe1s347bsbbv7o355evho.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Logueado correcto con Google
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Logueo fallido con Google
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                }
            }
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }



    companion object {
        private const val TAG = "LoginActivity"
    }
}

