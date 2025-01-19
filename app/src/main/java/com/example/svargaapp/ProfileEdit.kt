package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.account.ResponseData
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
    private lateinit var txtNewPassword: TextInputLayout
    private lateinit var txtConfirmPassword: TextInputLayout

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
        txtNewPassword = findViewById(R.id.editTextNewPassword)
        txtConfirmPassword = findViewById(R.id.editTextConfirmNewPassword)

        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 0)
        val email = sharedPreferences.getString("email", "Unknown")
        val name = sharedPreferences.getString("name", "Unknown")
        val number = sharedPreferences.getString("phone_number", "Unknown")

        // Menampilkan data user dari LoginActivity (menggunakan companion object)
        txtEmail.setText(email)
        txtNama.setText(name)
        txtPhoneNumber.setText(number)  // Memperbarui untuk menampilkan phone number
        txtPassword.editText?.setText("") // Password tidak diset karena ini untuk edit

        // Menambahkan fungsionalitas untuk tombol back
        backButton.setOnClickListener {
            Log.d(TAG, "Back button pressed. Navigating back.")
            onBackPressed()  // Menangani aksi ketika tombol back ditekan
        }

        // Menambahkan aksi untuk tombol save
        btnSave.setOnClickListener {

            val newPassword = txtNewPassword.editText?.text.toString()
            val confirmPassword = txtConfirmPassword.editText?.text.toString()

            // Jika password lama diisi, password baru dan konfirmasi password harus diisi dan cocok
            val currentPassword = txtPassword.editText?.text.toString()

            if (currentPassword.isNotEmpty()) {
                // Password baru dan konfirmasi password harus diisi dan cocok jika password lama diisi
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this@ProfileEdit, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (newPassword != confirmPassword) {
                    Toast.makeText(this@ProfileEdit, "Kata sandi baru tidak sama", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Menambahkan log sebelum melakukan request
            Log.d(TAG, "Sending profile update request with email: ${txtEmail.text}, name: ${txtNama.text}, phone number: ${txtPhoneNumber.text}, new password: $newPassword")

            // Mengirim data ke API menggunakan Retrofit
            RetrofitClient.instance.updateProfile(
                userId,
                txtEmail.text.toString(),
                txtNama.text.toString(),
                txtPhoneNumber.text.toString(),
                currentPassword,  // Mengirimkan password lama
                newPassword // Mengirimkan password baru jika ada
            ).enqueue(object : Callback<ResponseData> {

                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    // Memeriksa apakah response sukses
                    if (response.isSuccessful) {
                        val account = response.body()
                        if (account != null) {
                            Log.d(TAG, "Profile update successful. Response message: ${account.message}")
                            Toast.makeText(this@ProfileEdit, account.message, Toast.LENGTH_SHORT).show()

                            // Simpan data baru yang telah diperbarui ke dalam aplikasi
                            val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("email", txtEmail.text.toString())
                            editor.putString("name", txtNama.text.toString())
                            editor.putString("phone_number", txtPhoneNumber.text.toString())
                            if (newPassword.isNotEmpty()) {
                                editor.putString("password", newPassword) // Simpan password baru
                            }
                            editor.apply()

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


