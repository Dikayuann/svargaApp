package com.example.svargaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.svargaapp.R
import com.google.android.material.textfield.TextInputLayout

class ProfileDetail : AppCompatActivity() {

    // Deklarasi elemen-elemen UI yang akan diakses
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_detail)

        // Mengakses elemen tombol back dengan findViewById()
        backButton = findViewById(R.id.backButton)

        // Menambahkan fungsionalitas untuk tombol back
        backButton.setOnClickListener {
            onBackPressed()  // Menangani aksi ketika tombol back ditekan
        }

        // Mengambil data dari SharedPreferences
        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "N/A")
        val email = sharedPreferences.getString("username", "N/A")
        val phoneNumber = sharedPreferences.getString("number", "N/A")
        val password = sharedPreferences.getString("password", "N/A")

        // Menampilkan data pada EditText
        findViewById<EditText>(R.id.editTextName).setText(name)
        findViewById<EditText>(R.id.editTextEmail).setText(email)
        findViewById<EditText>(R.id.editTextPhoneNumber).setText(phoneNumber)
        findViewById<TextInputLayout>(R.id.editTextPassword).editText?.setText(password)
    }

    // Aksi default ketika tombol back ditekan
    override fun onBackPressed() {
        super.onBackPressed() // Navigasi kembali ke aktivitas sebelumnya
    }
}
