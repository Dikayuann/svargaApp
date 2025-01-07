package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.svargaapp.LoginActivity.Companion.name
import com.example.svargaapp.LoginActivity.Companion.number
import com.example.svargaapp.LoginActivity.Companion.password
import com.example.svargaapp.LoginActivity.Companion.username
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.account.ResponseData
import com.example.svargaapp.response.account.UpdateRequest
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEdit : AppCompatActivity() {

    // Deklarasi elemen-elemen UI yang akan diakses
    private lateinit var backButton: ImageView
    private lateinit var btnSave: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtNama: EditText
    private lateinit var txtPhoneNumber: EditText
    private lateinit var txtPassword: TextInputLayout

    private val TAG = "ProfileEdit"  // Untuk tag log

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_edit)

        // Mengakses elemen tombol back dan save
        backButton = findViewById(R.id.backButton)
        btnSave = findViewById(R.id.button)

        // Mengakses EditText untuk input user
        txtEmail = findViewById(R.id.editTextEmaill)
        txtNama = findViewById(R.id.editTextName1)
        txtPhoneNumber = findViewById(R.id.editTextPhoneNumber1)
        txtPassword = findViewById(R.id.editTextPassword)

        // Menampilkan data user dari LoginActivity (menggunakan companion object)
        txtEmail.setText(LoginActivity.username)
        txtNama.setText(LoginActivity.name)
        txtPhoneNumber.setText(LoginActivity.number)  // Memperbarui untuk menampilkan phone number
        txtPassword.editText?.setText(LoginActivity.password)

        // Menambahkan fungsionalitas untuk tombol back
        backButton.setOnClickListener {
            Log.d(TAG, "Back button pressed. Navigating back.")
            onBackPressed()  // Menangani aksi ketika tombol back ditekan
        }

        // Menambahkan aksi untuk tombol save
        btnSave.setOnClickListener {
            // Validasi input
            if (txtEmail.text.isNullOrEmpty() || txtNama.text.isNullOrEmpty() || txtPhoneNumber.text.isNullOrEmpty() || txtPassword.editText?.text.isNullOrEmpty()) {
                Toast.makeText(this@ProfileEdit, "Please fill all fields", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Validation failed: All fields must be filled.")
                return@setOnClickListener
            }

            // Menambahkan log sebelum melakukan request
            Log.d(TAG, "Sending profile update request with email: ${txtEmail.text}, name: ${txtNama.text}, phone number: ${txtPhoneNumber.text}")

            // Mengirim data ke API menggunakan Retrofit
            RetrofitClient.instance.updateProfile(
                txtEmail.text.toString(),
                txtNama.text.toString(),
                txtPhoneNumber.text.toString(),
                txtPassword.editText?.text.toString()
            ).enqueue(object : Callback<ResponseData> {

                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    // Memeriksa apakah response sukses
                    if (response.isSuccessful) {
                        val account = response.body()
                        if (account != null) {
                            Log.d(TAG, "Profile update successful. Response message: ${account.message}")
                            Toast.makeText(this@ProfileEdit, account.message, Toast.LENGTH_SHORT).show()

                            // Simpan data baru yang telah diperbarui ke dalam aplikasi
                            username = txtEmail.text.toString()
                            name = txtNama.text.toString()
                            number = txtPhoneNumber.text.toString()
                            password = txtPassword.editText?.text.toString()


                        } else {
                            Log.e(TAG, "Response body is null.")
                        }
                    } else {
                        // Jika response gagal, tampilkan log error dan pesan ke pengguna
                        Log.e(TAG, "Profile update failed. Response code: ${response.code()}, Response message: ${response.message()}")
                        Toast.makeText(this@ProfileEdit, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    // Jika permintaan gagal (misalnya karena masalah jaringan), tampilkan log dan pesan kesalahan
                    Log.e(TAG, "Profile update failed. Error: ${t.message}", t)
                    Toast.makeText(this@ProfileEdit, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    // Aksi default ketika tombol back ditekan
    override fun onBackPressed() {
        super.onBackPressed() // Navigasi kembali ke aktivitas sebelumnya
        Log.d(TAG, "Back pressed, returning to the previous activity.")
    }
}

