package com.example.svargaapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.EditText
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

//    companion object {
//        var username = "wongtulus@amikom.ac.id"
//        var password = "admin"
//    }

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
        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        val textViewRegisterPrompt: TextView = findViewById(R.id.textViewRegisterPrompt)


        buttonLogin.setOnClickListener {

            var user = editTextUsername.text.toString().trim()
            var pwd = editTextPassword.text.toString().trim()

            if (user.isEmpty()) {
                editTextUsername.error = "Username is required"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }
            if (pwd.isEmpty()) {
                editTextPassword.error = "Password is required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            //get response
            RetrofitClient.instance.postLogin(user, pwd).enqueue(
                object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val account = response.body()
                        if (account?.success == true) {
                            Toast.makeText(
                                this@LoginActivity,
                                account.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                account?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                }
            )


//            if (editTextUsername.text.toString().equals(username) &&
//                editTextPassword.text.toString().equals(password)) {
//                val intent = Intent(this, HomeActivity::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Login failed, Check your email and password",
//                    Toast.LENGTH_SHORT).show()
//            }
        }
        textViewRegisterPrompt.text = Html.fromHtml(getString(R.string.register_prompt), Html.FROM_HTML_MODE_LEGACY)

        //  click listener untuk mengarahkan ke halaman registrasi
        textViewRegisterPrompt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}