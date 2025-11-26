package com.example.loginproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgregarNoticiaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var idNoticiaEditar: String? = null // Variable para saber si editamos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_noticia)

        val etTitulo = findViewById<TextInputEditText>(R.id.etTitulo)
        val etBajada = findViewById<TextInputEditText>(R.id.etBajada)
        val etCuerpo = findViewById<TextInputEditText>(R.id.etCuerpo)
        val etUrlImagen = findViewById<TextInputEditText>(R.id.etUrlImagen)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarNoticia)
        val tvTituloPantalla = findViewById<TextView>(R.id.tvTituloPantalla) // Asegúrate de tener este ID o usa el textview directo

        // VERIFICAR SI VENIMOS A EDITAR
        // Si recibimos datos por el Intent, significa que es modo EDICIÓN
        if (intent.hasExtra("idNoticia")) {
            idNoticiaEditar = intent.getStringExtra("idNoticia")
            etTitulo.setText(intent.getStringExtra("titulo"))
            etBajada.setText(intent.getStringExtra("bajada"))
            etCuerpo.setText(intent.getStringExtra("cuerpo"))
            etUrlImagen.setText(intent.getStringExtra("urlImagen"))

            btnGuardar.text = "Actualizar Noticia" // Cambiamos el texto del botón
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val bajada = etBajada.text.toString()
            val cuerpo = etCuerpo.text.toString()
            val urlImagen = etUrlImagen.text.toString()

            if (titulo.isEmpty() || bajada.isEmpty() || cuerpo.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val datosNoticia = hashMapOf(
                "titulo" to titulo,
                "bajada" to bajada,
                "cuerpo" to cuerpo,
                "urlImagen" to urlImagen,
                // Si es edición, mantenemos la fecha original o la actualizamos (tú decides)
                // Aquí pondré fecha actual para simplificar
                "fecha" to SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                "autor" to "Alumno INACAP"
            )

            if (idNoticiaEditar != null) {
                // --- MODO ACTUALIZAR ---
                db.collection("noticias").document(idNoticiaEditar!!).update(datosNoticia as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Noticia actualizada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // --- MODO CREAR ---
                db.collection("noticias").add(datosNoticia)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Noticia creada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al crear", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}