package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.cart.CartResponse
import com.example.svargaapp.response.order.OrderRequest
import com.example.svargaapp.response.order.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), CartAdapter.TotalPriceListener {

    private val listMenu = ArrayList<CartItem>()
    private lateinit var textTotal: TextView
    private lateinit var textTaxPrice: TextView
    private lateinit var textGrandTotal: TextView
    private lateinit var btnCheckout: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        textTotal = view.findViewById(R.id.textTotalPrice)
        textTaxPrice = view.findViewById(R.id.textTaxPrice)
        textGrandTotal = view.findViewById(R.id.grandTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        val userId = LoginActivity.user_id.toString()

        // Initialize RecyclerView
        val RVMenu = view.findViewById<RecyclerView>(R.id.rvProducts)
        RVMenu.layoutManager = LinearLayoutManager(context)

        // Fetch cart items from API
        RetrofitClient.instance.getCartItems(userId).enqueue(object : Callback<ArrayList<CartItem>> {
            override fun onResponse(
                call: Call<ArrayList<CartItem>>,
                response: Response<ArrayList<CartItem>>
            ) {
                if (isAdded) { // Check if the fragment is still attached
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            // Clear current items and add the updated items
                            listMenu.clear()
                            listMenu.addAll(body)

                            // Update the adapter
                            val adapter = CartAdapter(listMenu, this@CartFragment, requireContext())
                            val RVMenu = view?.findViewById<RecyclerView>(R.id.rvProducts)
                            RVMenu?.adapter = adapter

                            // Calculate total, tax, and grand total
                            calculateTotals()
                        } else {
                            Log.e("CartFragment", "Response body is null")
                        }
                    } else {
                        Log.e("CartFragment", "Response is not successful: ${response.code()} ${response.message()}")
                    }
                } else {
                    Log.e("CartFragment", "Fragment is not attached. Skipping UI update.")
                }
            }

            override fun onFailure(call: Call<ArrayList<CartItem>>, t: Throwable) {
                Log.e("CartFragment", "API call failed", t)
            }
        })

        btnCheckout.setOnClickListener {
            val totalAmount = textGrandTotal.text.toString().replace("Rp ", "").replace(",", "").toDouble()

            // Membuat order baru
            val order = OrderRequest(
                user_id = LoginActivity.user_id,
                total = totalAmount,  // totalAmount sudah dihitung di UI
                location = LoginActivity.location
            )

            // Mengirim request untuk membuat order
            RetrofitClient.instance.createOrder(order).enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        val orderId = response.body()?.order_id ?: return
                        Log.d("CartFragment", "Order created successfully with ID: $orderId")
                        // Passing the order_id to TransactionFragment using Fragment arguments
                        val transactionFragment = TransactionFragment()
                        val bundle = Bundle()
                        bundle.putInt("order_id", orderId)
                        transactionFragment.arguments = bundle

                        // Passing the total to TransactionFragment using Fragment arguments
                        bundle.putInt("total", totalAmount.toInt())
                        transactionFragment.arguments = bundle

                        // Call HomeActivity method to update bottom navigation
                        activity?.let { activity ->
                            // Call HomeActivity method to update bottom navigation
                            (activity as? HomeActivity)?.updateBottomNavigationOnCheckout(
                                activity.findViewById(R.id.imageView4) // Accessing the "Transaction" icon
                            )
                        }

                        // Switch to the TransactionFragment
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, transactionFragment)
                            .addToBackStack(null)
                            .commit()

                    } else {
                        Log.e("CartFragment", "Failed to create order. Response Code: ${response.code()} - ${response.message()}")
                        Log.e("CartFragment", "Response Body: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Log.e("CartFragment", "Error creating order", t)
                }
            })
        }


    }

    private fun calculateTotals() {
        var total = 0.0
        listMenu.forEach { item ->
            total += item.price.toDouble() * item.quantity // Multiply price by quantity
        }

        // Calculate tax (12%)
        val tax = total * 0.12

        // Grand total
        val grandTotal = total + tax

        // Update the UI with calculated values
        textTotal.text = "Rp ${formatCurrency(total)}"
        textTaxPrice.text = "Rp ${formatCurrency(tax)}"
        textGrandTotal.text = "Rp ${formatCurrency(grandTotal)}"
    }

    private fun formatCurrency(amount: Double): String {
        // Formatting the currency with two decimal places
        return String.format("%,.0f", amount) // Ensure that the amount is formatted correctly
    }

    // Implementing the interface method from CartAdapter
    override fun onTotalPriceChanged() {
        // Recalculate totals whenever quantity changes
        calculateTotals()
    }

}
