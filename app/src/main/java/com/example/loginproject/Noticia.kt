package com.example.loginproject // Asegúrate que este sea tu paquete

data class Noticia(
    var id: String = "",           // ID del documento en Firebase
    val titulo: String = "",       // Título de la noticia
    val bajada: String = "",       // Resumen breve
    val cuerpo: String = "",       // Contenido completo
    val urlImagen: String = "",    // URL de la foto (opcional)
    val fecha: String = "",        // Fecha de publicación
    val autor: String = ""         // Quién la escribió
)