package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R

class InicioActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 1250

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val textViewNutriFit = findViewById<TextView>(R.id.textViewNutriFit)
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up_down)
        textViewNutriFit.startAnimation(scaleAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@InicioActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
