package com.example.loginproject

// import com.example.loginproject.R
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.EditText // ¡¡IMPORTANTE!! (Añadido para que reconozca el EditText)
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_login)

        // 1. Referencias a los componentes en el layout

        // ¡¡CAMBIO IMPORTANTE!!
        // Ahora buscamos el ID "editTextEmail" (tu campo "Usuario")
        val editTextUsername = findViewById<EditText>(R.id.editTextEmail)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textForgotPassword = findViewById<TextView>(R.id.textForgotPassword)
        val textRegisterHere = findViewById<TextView>(R.id.textRegisterHere)

        // 2. Lógica de Navegación (Rubrica #4 - Interconexión)

        // ¡¡BLOQUE MODIFICADO!!
        // Ahora abre 'WelcomeActivity' y le pasa el nombre de usuario
        buttonLogin.setOnClickListener {
            // 1. Capturamos el texto de tu campo "Usuario" (editTextEmail)
            val username = editTextUsername.text.toString()

            // 2. Creamos el Intent (el "paquete") para ir a WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)

            // 3. Metemos el nombre de usuario en el "paquete"
            intent.putExtra("EXTRA_USERNAME", username)

            // 4. Enviamos el "paquete" (iniciamos la nueva pantalla)
            startActivity(intent)

            // (Opcional: puedes descomentar 'finish()' si no quieres que
            // el usuario pueda volver al Login con el botón "atrás" del teléfono)
            // finish()
        }

        textForgotPassword.setOnClickListener {
            // Inicia la Actividad de Recuperar Clave
            // ¡¡CORREGIDO!! (Para que coincida con tu archivo "RecuperarContrasenaActivity.kt")
            val intent = Intent(this, RecuperarContrasenaActivity::class.java)
            startActivity(intent)
        }

        textRegisterHere.setOnClickListener {
            // Inicia la Actividad de Registrar
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 3. Bonus: Chequeo de Bluetooth (Se queda igual)
        checkBluetoothStatus()
    }

    /**
     * Función reutilizable para mostrar un AlertDialog.
     * (Se queda igual)
     */
    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    /**
     * BONUS: Simula la conexión inalámbrica (Bluetooth)
     * (Se queda igual)
     */
    private fun checkBluetoothStatus() {
        // Para interactuar con Bluetooth, necesitamos el Adapter
        // Usamos un try-catch por si los permisos no están en el Manifest
        // (Rubrica #3 - Seguridad básica)
        try {
            // Chequeo de permisos para Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Si no tenemos permiso, lo pedimos.
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        101
                    )
                    return
                }
            }

            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            if (bluetoothAdapter == null) {
                // El dispositivo no soporta Bluetooth
                showAlertDialog(
                    getString(R.string.bt_title),
                    getString(R.string.bt_not_supported)
                )
            } else {
                if (!bluetoothAdapter.isEnabled) {
                    // Bluetooth está apagado
                    showAlertDialog(
                        getString(R.string.bt_title),
                        getString(R.string.bt_not_enabled)
                    )
                } else {
                    // Bluetooth está encendido y listo
                    Log.d("Bluetooth", "Bluetooth está activado y listo.")
                }
            }
        } catch (e: SecurityException) {
            Log.e("LoginActivity", "Error de permisos de Bluetooth. ¿Están en el Manifest?", e)
            showAlertDialog(
                getString(R.string.bt_title),
                "Error de seguridad. Revisa los permisos de Bluetooth en el Manifest."
            )
        }
    }
}