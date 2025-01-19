package com.example.svargaapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.cart.CartRequest
import com.example.svargaapp.response.cart.CartResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(
    private val productList: ArrayList<CartItem>,
    private val totalPriceListener: TotalPriceListener,
    private val context: Context // Menambahkan context ke adapter
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    interface TotalPriceListener {
        fun onTotalPriceChanged()
    }

    inner class CartViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val id: TextView = v.findViewById(R.id.menuId)
        val textName: TextView = v.findViewById(R.id.menuName)
        val textPrice: TextView = v.findViewById(R.id.menuPrice)
        val textQuantity: TextView = v.findViewById(R.id.Quantity)
        val CartImage: ImageView = v.findViewById(R.id.imageMenuCart)
        val btnIncrease: ImageView = v.findViewById(R.id.btnIncrease)
        val btnDecrease: ImageView = v.findViewById(R.id.btnDecrease)

        fun bind(response: CartItem) {
            val cartId = "${response.cart_id}"
            val name = "${response.name}"
            val quantity = "${response.quantity}"
            val price = "Rp " + formatPrice(response.price)
            val picture = "${response.image}"

            id.text = cartId
            textName.text = name
            textPrice.text = price
            textQuantity.text = quantity

            // Set image using Picasso
            val url = RetrofitClient.IMAGE_URL + picture
            Picasso.get().load(url).into(CartImage)

            // Set listeners for quantity change buttons
            btnIncrease.setOnClickListener {
                response.quantity++ // Increase quantity
                textQuantity.text = response.quantity.toString() // Update quantity on UI
                totalPriceListener.onTotalPriceChanged() // Notify fragment to recalculate total
                updateCartItemOnServer(response) // Update server with new quantity
            }

            btnDecrease.setOnClickListener {
                if (response.quantity > 1) {
                    response.quantity-- // Decrease quantity
                    textQuantity.text = response.quantity.toString() // Update quantity on UI
                    totalPriceListener.onTotalPriceChanged() // Notify fragment to recalculate total
                    updateCartItemOnServer(response) // Update server with new quantity
                } else {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val itemView = itemView
                        val fadeOut = ObjectAnimator.ofFloat(itemView, "alpha", 1f, 0f)
                        fadeOut.duration = 500
                        fadeOut.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                productList.removeAt(position)  // Remove item from the list
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, itemCount)  // Recalculate total price
                                totalPriceListener.onTotalPriceChanged() // Recalculate total after removal
                                removeCartItemFromServer(response) // Remove item from server
                            }
                        })
                        fadeOut.start()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    private fun updateCartItemOnServer(cartItem: CartItem) {
        // Mendapatkan data dari SharedPreferences menggunakan context yang diteruskan ke adapter
        val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 0)

        val menuId = cartItem.menu_id
        val quantity = cartItem.quantity

        val cartRequest = CartRequest(userId, menuId, quantity)

        Log.d("CartAdapter", "Updating cart item with cartId: ${cartItem.cart_id}, quantity: ${cartItem.quantity}")

        // Kirim permintaan ke server menggunakan Retrofit
        RetrofitClient.instance.updateCartItem(cartItem.cart_id, cartRequest)
            .enqueue(object : Callback<CartResponse> {
                override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                    if (response.isSuccessful) {
                        Log.d("CartAdapter", "Cart item updated on the server: ${response.body()}")
                    } else {
                        Log.e("CartAdapter", "Failed to update cart item on the server.")
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Log.e("CartAdapter", "Error updating cart item on the server.", t)
                }
            })
    }

    private fun removeCartItemFromServer(cartItem: CartItem) {
        // Mendapatkan data dari SharedPreferences menggunakan context yang diteruskan ke adapter
        val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 0)

        // Panggil API untuk menghapus item dari server
        RetrofitClient.instance.removeCartItem(userId, cartItem.cart_id)
            .enqueue(object : Callback<CartResponse> {
                override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                    if (response.isSuccessful) {
                        Log.d("CartAdapter", "Cart item removed from server successfully.")
                    } else {
                        Log.e("CartAdapter", "Failed to remove cart item from server.")
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Log.e("CartAdapter", "Error removing cart item from server.", t)
                }
            })
    }

    private fun formatPrice(amount: Double): String {
        // Formatting the currency with two decimal places
        return String.format("%,.0f", amount)
    }
}