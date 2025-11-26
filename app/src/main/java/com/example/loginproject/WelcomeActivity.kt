package com.example.loginproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class WelcomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val listaNoticias = mutableListOf<Noticia>()
    private lateinit var adapter: NoticiasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Configurar el RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recyclerNoticias)
        recycler.layoutManager = LinearLayoutManager(this)

        // Inicializamos el adapter.
        // Cuando se haga click en una noticia, abriremos "VerNoticiaActivity" (que crearemos luego)
        adapter = NoticiasAdapter(listaNoticias) { noticia ->
            val intent = Intent(this, VerNoticiaActivity::class.java)
            intent.putExtra("idNoticia", noticia.id) // Pasamos el ID para cargar el detalle allá
            startActivity(intent)
        }
        recycler.adapter = adapter

        // Botón Agregar (+)
        findViewById<FloatingActionButton>(R.id.fabAgregar).setOnClickListener {
            startActivity(Intent(this, AgregarNoticiaActivity::class.java))
        }

        // Botón Cerrar Sesión
        findViewById<ImageButton>(R.id.btnCerrarSesion).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Cargar datos de Firebase
        cargarNoticias()
    }

    private fun cargarNoticias() {
        // Escuchamos la colección "noticias" en tiempo real
        db.collection("noticias")
            .orderBy("fecha", Query.Direction.DESCENDING) // Ordenar por fecha (opcional)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                listaNoticias.clear() // Limpiamos la lista local para no duplicar
                for (document in value!!) {
                    // Convertimos el documento de Firebase a nuestro objeto Noticia
                    val noticia = document.toObject(Noticia::class.java)
                    noticia.id = document.id // Guardamos el ID real del documento
                    listaNoticias.add(noticia)
                }
                // Avisamos al adaptador que los datos cambiaron
                adapter.notifyDataSetChanged()
            }
    }
}