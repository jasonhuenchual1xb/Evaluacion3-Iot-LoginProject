package com.example.loginproject

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        // 1. Referencias a los componentes en el layout
        val editTextUsername = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textForgotPassword = findViewById<TextView>(R.id.textForgotPassword)
        val textRegisterHere = findViewById<TextView>(R.id.textRegisterHere)
        // 🛑 ELIMINADO: La referencia al btnLanzarEV4 ya no va aquí.
        // val btnLanzarEV4 = findViewById<Button>(R.id.btnLanzarEV4)

        // 2. Lógica de Navegación

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // VALIDACIÓN DE CAMPOS VACÍOS
            if (username.isEmpty() || password.isEmpty()) {
                showToast("Por favor, ingresa tu usuario y contraseña.")
                return@setOnClickListener
            }

            // LÓGICA DE FIREBASE: Verifica que la cuenta sea REAL
            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        // ÉXITO: Navegar a WelcomeActivity (donde estará el botón EV4)
                        showToast("¡Inicio de sesión exitoso!")
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.putExtra("EXTRA_USERNAME", username)
                        startActivity(intent)
                        finish()

                    } else {
                        showToast("Error: Credenciales inválidas o cuenta no registrada.")
                    }
                }
        }

        textForgotPassword.setOnClickListener {
            val intent = Intent(this, RecuperarContrasenaActivity::class.java)
            startActivity(intent)
        }

        textRegisterHere.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 🛑 ELIMINADO: El Listener del botón btnLanzarEV4 ya no va aquí.
        /*
        btnLanzarEV4.setOnClickListener {
            val intent = Intent(this, Evaluacion4IotActivity::class.java)
            startActivity(intent)
        }
        */

        // 3. Chequeo de Bluetooth
        checkBluetoothStatus()
    }

    // ... (Las funciones showAlertDialog y checkBluetoothStatus se quedan igual) ...
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

    private fun checkBluetoothStatus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
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
                showAlertDialog(
                    getString(R.string.bt_title),
                    getString(R.string.bt_not_supported)
                )
            } else {
                if (!bluetoothAdapter.isEnabled) {
                    showAlertDialog(
                        getString(R.string.bt_title),
                        getString(R.string.bt_not_enabled)
                    )
                } else {
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