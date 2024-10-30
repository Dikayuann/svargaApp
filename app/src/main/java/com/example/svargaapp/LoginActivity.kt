package com.example.svargaapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    companion object {
        var username = "wongtulus@amikom.ac.id"
        var password = "admin"


    }

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
            if (editTextUsername.text.toString().equals(username) &&
                editTextPassword.text.toString().equals(password)) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login failed, Check your email and password",
                    Toast.LENGTH_SHORT).show()
            }
        }
        textViewRegisterPrompt.text = Html.fromHtml(getString(R.string.register_prompt), Html.FROM_HTML_MODE_LEGACY)

        //  click listener untuk mengarahkan ke halaman registrasi
        textViewRegisterPrompt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
