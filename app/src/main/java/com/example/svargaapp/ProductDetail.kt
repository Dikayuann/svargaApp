package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.cart.CartRequest
import com.example.svargaapp.response.cart.CartResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetail : AppCompatActivity() {
    private lateinit var btnAddToCart: TextView

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


        // Mengambil data quantity
        val quantityTextView: TextView = findViewById(R.id.quantity)
        val quantity = quantityTextView.text.toString().toInt()

// Ambil data dari intent
        val userId = LoginActivity.user_id.toString()
        Log.d("ProductDetail", "User ID: $userId")
        val menuId = foodId.toString()
        Log.d("ProductDetail", "Menu ID: $menuId")
        val cartRequest = CartRequest(userId, menuId, quantity)
        Log.d("ProductDetail", "Cart Request: $cartRequest")


        btnAddToCart = findViewById(R.id.btnCheckout)
        btnAddToCart.setOnClickListener {


// Memanggil API untuk menambahkan ke cart
            RetrofitClient.instance.addToCart(cartRequest).enqueue(object : Callback<CartResponse> {
                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(applicationContext, "Item added to cart", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to add item to cart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }

    }
}