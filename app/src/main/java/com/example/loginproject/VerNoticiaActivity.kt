package com.example.loginproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class VerNoticiaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var noticiaActual: Noticia? = null // Guardamos la noticia para poder editarla

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_noticia)

        val btnAtras = findViewById<ImageButton>(R.id.btnAtrasDetalle)
        val btnEditar = findViewById<Button>(R.id.btnEditar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        val tvTitulo = findViewById<TextView>(R.id.tvDetalleTitulo)
        val tvBajada = findViewById<TextView>(R.id.tvDetalleBajada)
        val tvCuerpo = findViewById<TextView>(R.id.tvDetalleCuerpo)
        val ivImagen = findViewById<ImageView>(R.id.ivDetalleImagen)

        btnAtras.setOnClickListener { finish() }

        val idNoticia = intent.getStringExtra("idNoticia")

        if (idNoticia != null) {
            // Cargar datos
            db.collection("noticias").document(idNoticia).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        noticiaActual = document.toObject(Noticia::class.java)
                        noticiaActual?.id = document.id // Aseguramos tener el ID

                        if (noticiaActual != null) {
                            tvTitulo.text = noticiaActual!!.titulo
                            tvBajada.text = noticiaActual!!.bajada
                            tvCuerpo.text = noticiaActual!!.cuerpo

                            if (noticiaActual!!.urlImagen.isNotEmpty()) {
                                Glide.with(this).load(noticiaActual!!.urlImagen).into(ivImagen)
                            }
                        }
                    }
                }

            // --- LÓGICA ELIMINAR ---
            btnEliminar.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Eliminar Noticia")
                    .setMessage("¿Estás seguro de que quieres borrar esta noticia?")
                    .setPositiveButton("Sí, borrar") { _, _ ->
                        db.collection("noticias").document(idNoticia).delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Noticia eliminada", Toast.LENGTH_SHORT).show()
                                finish() // Volvemos al Home
                            }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            // --- LÓGICA EDITAR ---
            btnEditar.setOnClickListener {
                if (noticiaActual != null) {
                    val intent = Intent(this, AgregarNoticiaActivity::class.java)
                    // Le pasamos los datos actuales para que los rellene
                    intent.putExtra("idNoticia", idNoticia)
                    intent.putExtra("titulo", noticiaActual!!.titulo)
                    intent.putExtra("bajada", noticiaActual!!.bajada)
                    intent.putExtra("cuerpo", noticiaActual!!.cuerpo)
                    intent.putExtra("urlImagen", noticiaActual!!.urlImagen)
                    startActivity(intent)
                    finish() // Cerramos esta pantalla para que al volver se recargue con los cambios
                }
            }
        }
    }
}