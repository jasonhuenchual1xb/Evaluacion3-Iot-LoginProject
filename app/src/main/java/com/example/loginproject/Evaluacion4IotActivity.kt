package com.example.loginproject

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.bluetooth.BluetoothAdapter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Evaluacion4IotActivity : AppCompatActivity() {

    private lateinit var switchBluetooth: Switch
    private lateinit var switchWifi: Switch
    private lateinit var tvAngulo: TextView
    private lateinit var tvEstadoAlerta: TextView
    private lateinit var osmMapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var wifiManager: WifiManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 101
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluacion_4_iot)

        // Inicialización de Gestores y Cliente de Ubicación
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicialización de Vistas (Conexión con el XML)
        switchBluetooth = findViewById(R.id.switchBluetooth)
        switchWifi = findViewById(R.id.switchWifi)
        tvAngulo = findViewById(R.id.tvAngulo)
        tvEstadoAlerta = findViewById(R.id.tvEstadoAlerta)

        // Lógica de Componentes
        requestPermissions()
        setupNetworkSwitches()
        simulateAccelerometerData()

        // Inicializar Mapa de OSMDroid
        setupOSMMap()
    }

    private fun requestPermissions() {
        // Solicitud de permisos de ubicación y Bluetooth (esencial para que el rastreo funcione)
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun setupNetworkSwitches() {
        // Lógica para Switches (Bluetooth y WiFi)
        if (bluetoothAdapter != null) {
            // Se asume que los permisos BLUETOOTH_CONNECT ya fueron solicitados
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                switchBluetooth.isChecked = bluetoothAdapter.isEnabled
                switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) { bluetoothAdapter.enable() } else { bluetoothAdapter.disable() }
                }
            }
        }
        // Nota: isWifiEnabled está en desuso (deprecated), pero funciona para la EV4.
        switchWifi.isChecked = wifiManager.isWifiEnabled
        switchWifi.setOnCheckedChangeListener { _, isChecked ->
            wifiManager.isWifiEnabled = isChecked
        }
    }

    // ... (Tu lógica de simulación de acelerómetro y UI se mantiene igual)
    private fun simulateAccelerometerData() = scope.launch {
        while (true) {
            val currentAngle = Random.nextInt(0, 181).toFloat()
            withContext(Dispatchers.Main) {
                updateAccelerometerUI(currentAngle)
            }
            kotlinx.coroutines.delay(3000)
        }
    }

    private fun updateAccelerometerUI(angle: Float) {
        tvAngulo.text = "Ángulo Actual: ${angle.toInt()}°"

        when {
            angle >= 170 && angle <= 180 -> {
                tvEstadoAlerta.text = "¡ALERTA: INVERSIÓN TOTAL (180°)"
                tvEstadoAlerta.setTextColor(getColor(android.R.color.holo_red_dark))
            }
            angle >= 80 && angle <= 100 -> {
                tvEstadoAlerta.text = "¡ADVERTENCIA: INCLINACIÓN (90°)"
                tvEstadoAlerta.setTextColor(getColor(android.R.color.holo_orange_dark))
            }
            else -> {
                tvEstadoAlerta.text = "Estado: Normal"
                tvEstadoAlerta.setTextColor(getColor(android.R.color.black))
            }
        }
    }
    // ...

    private fun setupOSMMap() {
        // Inicialización obligatoria de OSMDroid
        Configuration.getInstance().load(applicationContext,
            getSharedPreferences("org.osmdroid.pref", Context.MODE_PRIVATE))

        osmMapView = findViewById(R.id.mapview)
        osmMapView.setTileSource(TileSourceFactory.MAPNIK)
        osmMapView.setMultiTouchControls(true)

        // 🛑 PUNTO CRÍTICO: CENTRADO INICIAL EN AMÉRICA DEL SUR (Santiago, Chile)
        val mapController = osmMapView.controller
        // Latitud: -33.4489, Longitud: -70.6693
        val startPoint = GeoPoint(-33.4489, -70.6693)
        mapController.setZoom(15.0)
        mapController.setCenter(startPoint)

        // Iniciar el Rastreo de Ubicación continua
        startLocationTrackingForOSM()
    }


    private fun startLocationTrackingForOSM() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
            return
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 5000L).build() // Actualización cada 5 segundos

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val newGeoPoint = GeoPoint(location.latitude, location.longitude)

                    // 🛑 PUNTO CRÍTICO: RASTREO (Mueve el mapa al nuevo punto)
                    osmMapView.controller.animateTo(newGeoPoint)

                    // Actualizar la vista del mapa
                    osmMapView.invalidate()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    // ... (El ciclo de vida se mantiene igual)
    override fun onResume() {
        super.onResume()
        if (::osmMapView.isInitialized) osmMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::osmMapView.isInitialized) osmMapView.onPause()
    }
}