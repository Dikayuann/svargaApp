package com.example.svargaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.svargaapp.CartRepository.CartRepository

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Get items from CartRepository
        val cartItems = CartRepository.getCartItems()

        // Ensure cartItems is not empty or null
        if (cartItems.isNotEmpty()) {
            cartAdapter = CartAdapter(cartItems)
            recyclerView.adapter = cartAdapter
        } else {
            // Handle empty cart, maybe show a "Cart is empty" message
        }
    }
}
