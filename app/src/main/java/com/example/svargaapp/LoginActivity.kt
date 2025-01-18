package com.example.svargaapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.account.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mengambil referensi ke elemen UI
        val editTextUsername: EditText = findViewById(R.id.editTextEmailLogin)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        val textViewRegisterPrompt: TextView = findViewById(R.id.textViewRegisterPrompt)

        // Menambahkan progress bar
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        buttonLogin.setOnClickListener {
            // Menampilkan ProgressBar
            progressBar.visibility = View.VISIBLE

            var email = editTextUsername.text.toString().trim()
            var pwd = editTextPassword.text.toString().trim()

            val emailPattern = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
            if (email.isEmpty()) {
                editTextUsername.error = "Email is required"
                editTextUsername.requestFocus()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (!email.matches(emailPattern)) {
                editTextUsername.error = "Invalid email format"
                editTextUsername.requestFocus()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (pwd.isEmpty()) {
                editTextPassword.error = "Password is required"
                editTextPassword.requestFocus()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (pwd.length < 6) {
                editTextPassword.error = "Password must be at least 6 characters long"
                editTextPassword.requestFocus()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            //get response
            RetrofitClient.instance.postLogin(email, pwd).enqueue(
                object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        progressBar.visibility = View.GONE
                        val account = response.body()
                        if (response.isSuccessful && account?.success == true) {
                            // Menyimpan data ke SharedPreferences dan melanjutkan ke halaman home
                            Toast.makeText(this@LoginActivity, account.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)

                            val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putInt("user_id", account.data.user_id)
                            editor.putString("email", account.data.email)
                            editor.putString("name", account.data.name)
                            editor.putString("level", account.data.level)
                            editor.putString("location", account.data.location)
                            editor.putString("foto_profile", account.data.foto_profile)
                            editor.putString("phone_number", account.data.phone_number)
                            editor.putString("password", account.data.password)
                            editor.putBoolean("is_logged_in", true)
                            editor.apply()

                            Log.d("LoginActivity", "User ID: ${account.data.user_id}")
                        } else {
                            Toast.makeText(this@LoginActivity, account?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                }
            )
       }
        textViewRegisterPrompt.text = Html.fromHtml(getString(R.string.register_prompt), Html.FROM_HTML_MODE_LEGACY)

        //  click listener untuk mengarahkan ke halaman registrasi
        textViewRegisterPrompt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
