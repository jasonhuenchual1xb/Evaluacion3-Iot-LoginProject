package com.example.loginproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button // ¡Añadido para el botón EV4!
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class WelcomeActivity : AppCompatActivity() {

    // 🛑 CORRECCIÓN DE ERROR: Eliminamos 'nicolas' al final
    private val db = FirebaseFirestore.getInstance()
    private val listaNoticias = mutableListOf<Noticia>()
    private lateinit var adapter: NoticiasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asume que activity_welcome.xml ahora es tu layout de Noticias
        setContentView(R.layout.activity_welcome)

        // 1. Configurar el RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recyclerNoticias)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = NoticiasAdapter(listaNoticias) { noticia ->
            val intent = Intent(this, VerNoticiaActivity::class.java)
            intent.putExtra("idNoticia", noticia.id)
            startActivity(intent)
        }
        recycler.adapter = adapter

        // 2. Botón Agregar (+)
        findViewById<FloatingActionButton>(R.id.fabAgregar).setOnClickListener {
            startActivity(Intent(this, AgregarNoticiaActivity::class.java))
        }

        // 🌟 3. INTEGRACIÓN DEL BOTÓN VERDE DE EVALUACIÓN IOT 🌟
        val btnLanzarEV4 = findViewById<Button>(R.id.btnLanzarEV4)
        btnLanzarEV4.setOnClickListener {
            // Lanza la actividad de la Evaluación IoT con el mapa y sensores
            startActivity(Intent(this, Evaluacion4IotActivity::class.java))
        }

        // 4. Botón Cerrar Sesión
        findViewById<ImageButton>(R.id.btnCerrarSesion).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 5. Cargar datos de Firebase
        cargarNoticias()
    }

    private fun cargarNoticias() {
        // Escuchamos la colección "noticias" en tiempo real
        db.collection("noticias")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(this, "Error al cargar noticias: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                listaNoticias.clear()
                for (document in value!!) {
                    // Nota: Asegúrate de que la clase Noticia exista y sea un data class
                    val noticia = document.toObject(Noticia::class.java)
                    noticia.id = document.id
                    listaNoticias.add(noticia)
                }
                adapter.notifyDataSetChanged()
            }
    }
}