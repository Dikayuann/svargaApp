package com.example.svargaapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.svargaapp.client.RetrofitClient
import com.squareup.picasso.Picasso

class ProductDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get data from Intent
        val foodId = intent.getStringExtra("MENU_ID")
        val foodName = intent.getStringExtra("MENU_NAME")
        val foodSubtitle = intent.getStringExtra("MENU_SUBTITLE")
        val foodDescription = intent.getStringExtra("MENU_DESCRIPTION")
        val foodPrice = intent.getStringExtra("MENU_PRICE")
        val foodPicture = intent.getStringExtra("MENU_PICTURE")

        // Bind data to UI
        val textViewId: TextView = findViewById(R.id.productId)
        val textViewName: TextView = findViewById(R.id.productTitle)
        val textViewSubtitle: TextView = findViewById(R.id.productSubtitle)
        val textViewDescription: TextView = findViewById(R.id.productContent)
        val textViewPrice: TextView = findViewById(R.id.productPrice)
        val imageView: ImageView = findViewById(R.id.productImage)

        textViewId.text = foodId
        textViewName.text = foodName
        textViewSubtitle.text = foodSubtitle
        textViewDescription.text = foodDescription
        textViewPrice.text = foodPrice
        Picasso.get().load(RetrofitClient.IMAGE_URL + foodPicture).into(imageView)


        //Back Button
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}