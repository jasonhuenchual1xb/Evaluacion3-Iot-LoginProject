package com.example.loginproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // VERIFICACIÓN DE SEGURIDAD:
            // ¿Hay un usuario recordado en el celular?
            val usuarioActual = FirebaseAuth.getInstance().currentUser

            if (usuarioActual != null) {
                // SI existe usuario -> Lo mandamos directo al Home (Welcome)
                startActivity(Intent(this, WelcomeActivity::class.java))
            } else {
                // NO existe usuario -> Lo mandamos a Login
                startActivity(Intent(this, LoginActivity::class.java))
            }

            finish() // Cerramos el Splash para no volver
        }, 2000)
    }
}