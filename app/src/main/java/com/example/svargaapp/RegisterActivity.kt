package com.example.svargaapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mengambil referensi ke elemen UI
        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextConfirmPassword: EditText = findViewById(R.id.editTextConfirmPassword)
        val buttonRegister: Button = findViewById(R.id.buttonRegister)

        // Click listener untuk tombol register
        buttonRegister.setOnClickListener {
            validateInput(
                editTextUsername.text.toString(),
                editTextEmail.text.toString(),
                editTextPassword.text.toString(),
                editTextConfirmPassword.text.toString()
            )
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String) {
        // Validasi input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulasi penyimpanan data
        Toast.makeText(this, "Pendaftaran berhasil! Silakan login.", Toast.LENGTH_SHORT).show()

        // Mengarahkan ke halaman login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Menutup aktivitas pendaftaran
    }
}
