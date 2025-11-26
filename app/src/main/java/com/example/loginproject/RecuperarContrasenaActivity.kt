package com.example.loginproject
// import com.example.loginproject.R
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // <-- ¡¡CAMBIO 1: AÑADIDO!!

class RecuperarContrasenaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

        // --- ¡¡CAMBIO 2: AÑADIDO!! ---
        // 1. Buscamos la Toolbar que pusimos en el XML
        val toolbar = findViewById<Toolbar>(R.id.toolbarRecover)
        // 2. Le decimos a la Actividad: "Esta es tu barra de acción oficial"
        setSupportActionBar(toolbar)

        // Habilita el botón "atrás" en la barra superior (ActionBar)
        // (Esta línea ya la tenías, y ahora SÍ funcionará)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Recuperar Contraseña" // (Opcional: Ponle un título)


        val buttonRecover = findViewById<Button>(R.id.buttonRecover)

        buttonRecover.setOnClickListener {
            // Simulación de recuperación (Rubrica #2)
            showAlertDialog(
                getString(R.string.recover_sent_title),
                getString(R.string.recover_sent_message)
            )
        }
    }

    // Función para manejar el clic en el botón "atrás" de la barra
    // (Tu código original - ¡está perfecto!)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Cierra la actividad actual y regresa a la anterior (Login)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Función de alerta (copiada para esta actividad)
    // (Tu código original - ¡está perfecto!)
    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                // Opcional: podríamos cerrar la actividad después del OK
                // finish()
                dialog.dismiss()
            }
            .create()
            .show()
    }
}