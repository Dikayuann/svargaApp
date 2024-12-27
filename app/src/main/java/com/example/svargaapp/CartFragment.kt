package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.menu.MenuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    private val listMenu = ArrayList<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = LoginActivity.user_id.toString()
        Log.d("ProductDetail", "User ID: $userId")

        // Initialize RecyclerView
        val RVMenu = view.findViewById<RecyclerView>(R.id.rvProducts)
        RVMenu.layoutManager = LinearLayoutManager(context)

        // Fetch cart items from API
        RetrofitClient.instance.getCartItems(userId).enqueue(object : Callback<ArrayList<CartItem>> {
            override fun onResponse(
                call: Call<ArrayList<CartItem>>,
                response: Response<ArrayList<CartItem>>
            ) {
                // Log the whole response for debugging
                Log.d("CartFragment", "Received response: ${response.body()}")
                Log.d("CartFragment", "Response code: ${response.code()}")
                Log.d("CartFragment", "Response message: ${response.message()}")

                // Check if the response was successful
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Update list and adapter
                        listMenu.clear()
                        // Periksa item yang sudah ada dan tambahkan kuantitasnya jika perlu
                        body.forEach { newItem ->
                            val index = listMenu.indexOfFirst { it.menu_id == newItem.menu_id }
                            if (index != -1) {
                                listMenu[index].quantity += newItem.quantity
                            } else {
                                listMenu.add(newItem)
                            }
                        }
                        val adapter = CartAdapter(listMenu) // Set the adapter correctly
                        RVMenu.adapter = adapter
                    } else {
                        Log.e("CartFragment", "Response body is null")
                    }
                } else {
                    Log.e("CartFragment", "Response is not successful: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<CartItem>>, t: Throwable) {
                Log.e("CartFragment", "API call failed", t)
            }
        })
    }
}

