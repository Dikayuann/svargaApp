package com.example.svargaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.account.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Mengambil referensi ke elemen UI
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextUsername: EditText = findViewById(R.id.editTextEmail)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextConfirmPassword: EditText = findViewById(R.id.editTextConfirmPassword)
        val buttonRegister: Button = findViewById(R.id.buttonRegister)

        // Click listener untuk tombol register
        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            // Validasi input
            validateInput(username, name, password, confirmPassword)
        }
    }

    private fun validateInput(username: String, name: String, password: String, confirmPassword: String) {
        // Validasi input
        if (username.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Pastikan password dan confirm password cocok
        if (password != confirmPassword) {
            Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi panjang password minimal
        if (password.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        // Mengirim data ke API untuk registrasi
        registerUser(username, name, password, confirmPassword)
    }

    private fun registerUser(username: String, name: String, password: String, confirmPassword: String) {
        // Memanggil API untuk mendaftar pengguna menggunakan Retrofit
        val apiService = RetrofitClient.instance
        val call = apiService.registerUser(username, name, password, confirmPassword)

        // Mengirim request registrasi ke API
        call.enqueue(object : Callback<RegisterRequest> {
            override fun onResponse(call: Call<RegisterRequest>, response: Response<RegisterRequest>) {
                if (response.isSuccessful) {
                    // Menampilkan pesan sukses
                    Toast.makeText(this@RegisterActivity, "Pendaftaran berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Menampilkan pesan gagal
                    val errorMessage = response.errorBody()?.string() ?: response.message()
                    Toast.makeText(this@RegisterActivity, "Pendaftaran gagal: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterRequest>, t: Throwable) {
                // Menampilkan pesan kesalahan jika terjadi error
                Toast.makeText(this@RegisterActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
