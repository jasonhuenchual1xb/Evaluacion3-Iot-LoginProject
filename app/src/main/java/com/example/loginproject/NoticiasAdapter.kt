package com.example.loginproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView // ¡Importación nueva!
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // ¡Importación nueva!

// Este adaptador recibe la lista de noticias y una función "onClick"
class NoticiasAdapter(
    private val noticias: List<Noticia>,
    private val onClick: (Noticia) -> Unit
) : RecyclerView.Adapter<NoticiasAdapter.ViewHolder>() {

    // Esta clase "interno" guarda las referencias a los elementos visuales de CADA fila (item)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTituloItem)
        val bajada: TextView = view.findViewById(R.id.tvBajadaItem)
        val imagen: ImageView = view.findViewById(R.id.ivItemImagen)
        val card: View = view.findViewById(R.id.cardNoticia)
    }

    // Paso 1: "Inflar" el diseño.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        return ViewHolder(view)
    }

    // Paso 2: "Vincular" datos. Pone el texto y la imagen.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias[position]
        holder.titulo.text = noticia.titulo
        holder.bajada.text = noticia.bajada

        // --- CÓDIGO GLIDE PARA LA IMAGEN ---
        if (noticia.urlImagen.isNotEmpty()) {
            Glide.with(holder.imagen.context)
                .load(noticia.urlImagen)
                .centerCrop()
                .into(holder.imagen)
        } else {
            holder.imagen.setImageDrawable(null)
        }
        // ------------------------------------

        // Configurar el click
        holder.card.setOnClickListener {
            onClick(noticia)
        }
    }

    // Paso 3: Decirle a la lista cuántos elementos hay
    override fun getItemCount() = noticias.size
}