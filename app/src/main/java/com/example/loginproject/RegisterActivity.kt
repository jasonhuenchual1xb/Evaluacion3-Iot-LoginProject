package com.example.loginproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Asegúrate de tener este XML (paso 2)

        val etNombre = findViewById<EditText>(R.id.etNombreRegistro)
        val etEmail = findViewById<EditText>(R.id.etEmailRegistro)
        val etPass = findViewById<EditText>(R.id.etPassRegistro)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVolver = findViewById<Button>(R.id.btnVolverLogin) // Si tienes botón volver

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Crear usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    // El usuario se creó en Auth, ahora guardamos sus datos en Firestore
                    val uid = result.user?.uid ?: ""

                    val usuarioMap = hashMapOf(
                        "uid" to uid,
                        "nombre" to nombre,
                        "email" to email
                    )

                    // 2. Guardar en colección "usuarios" usando el mismo UID como ID del documento
                    db.collection("usuarios").document(uid).set(usuarioMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                            // Volvemos al Login para que ingrese
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Botón volver (opcional)
        btnVolver?.setOnClickListener { finish() }
    }
}