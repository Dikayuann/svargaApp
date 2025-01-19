package com.example.svargaapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.map.MapResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.tasks.OnSuccessListener
//import org.osmdroid.api.IMapController
//import org.osmdroid.views.MapView
//import org.osmdroid.util.GeoPoint
//import org.osmdroid.config.Configuration
//import org.osmdroid.views.MapView
//import org.osmdroid.util.GeoPoint
//import org.osmdroid.views.overlay.Marker

//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.tasks.OnSuccessListener
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.android.gms.location.LocationRequest
//import org.osmdroid.api.IMapController
//import org.osmdroid.config.Configuration
//import org.osmdroid.events.MapListener
//import org.osmdroid.events.ScrollEvent
//import org.osmdroid.events.ZoomEvent
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
//import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay



class MapActivity : AppCompatActivity() {
    private lateinit var txtLocation: TextView
    private lateinit var txtLocationNew: TextView
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)

        // Setting padding for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize location UI element
        txtLocation = findViewById(R.id.nowLocation)

        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        var location = sharedPreferences.getString("location", "")
        val userId = sharedPreferences.getInt("user_id", 0)

        txtLocation.text = location  // Set the current location from LoginActivity

        Log.e("MapActivity", "Before New Location: ${location}")

        // Make the app fullscreen
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        // Back Button
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        btnSave = findViewById(R.id.save)
        txtLocationNew = findViewById(R.id.searchLocation)

        Log.e("MapActivity", "user id: ${userId}")

        // Set up Save button functionality
        btnSave.setOnClickListener {
            val newLocation = txtLocationNew.text.toString()  // Get the new location text
            if (newLocation.isNotEmpty()) {
                // Call Retrofit API to save the new location
                RetrofitClient.instance.putLocation(userId, newLocation).enqueue(object : Callback<MapResponse> {
                    override fun onResponse(call: Call<MapResponse>, response: Response<MapResponse>) {
                        val account = response.body()
                        if (response.isSuccessful) {
                            val message = account?.message ?: "Location updated successfully"
                            Toast.makeText(this@MapActivity, message, Toast.LENGTH_SHORT).show()
                            // Update location in local variable
                            location = newLocation
                            Log.e("MapActivity", "New Location: ${location}")
                            txtLocation.text = location  // Update the UI with the new location

                            // Save to SharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putString("location", location)
                            editor.apply()

                            // Close MapActivity and go back to TransactionFragment
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            val message = account?.message ?: "Failed to update location"
                            Toast.makeText(this@MapActivity, message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<MapResponse>, t: Throwable) {
                        val errorMessage = t.message ?: "Unknown error occurred"
                        Toast.makeText(this@MapActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this@MapActivity, "Please enter a new location", Toast.LENGTH_SHORT).show()
            }
        }


    }
}



        // Mendapatkan MapView dan mengonfigurasi peta
//        mapView = findViewById(R.id.mapView)
//        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)

        // Mengaktifkan kontrol zoom dan sentuhan multi-gesture
//        mapView.setBuiltInZoomControls(true)
//        mapView.setMultiTouchControls(true)

        // Inisialisasi FusedLocationProviderClient untuk mendapatkan lokasi pengguna
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Memeriksa izin lokasi
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            // Izin sudah diberikan, langsung ambil lokasi
//            getUserLocation()
//        } else {
//            // Izin belum diberikan, minta izin
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }
//    }
//
//    private fun getUserLocation() {
//        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
//            if (location != null) {
//                // Memindahkan peta ke lokasi pengguna
//                val userLocation = GeoPoint(location.latitude, location.longitude)
//                val mapController: IMapController = mapView.controller
//                mapController.setZoom(14)
//                mapController.setCenter(userLocation)
//
//                // Menampilkan Toast dengan koordinat pengguna
//                Toast.makeText(this, "Lat: ${location.latitude}, Lon: ${location.longitude}", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    // Menangani hasil permintaan izin
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Izin diberikan, ambil lokasi
//                    getUserLocation()
//                } else {
//                    // Izin ditolak
//                    Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()  // Aktifkan kembali MapView
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()  // Matikan MapView saat aplikasi dihentikan
//    }
//}