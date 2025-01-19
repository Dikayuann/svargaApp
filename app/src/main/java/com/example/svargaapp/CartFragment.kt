package com.example.svargaapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.cart.CartItem
import com.example.svargaapp.response.order.OrderRequest
import com.example.svargaapp.response.order.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), CartAdapter.TotalPriceListener {

    private val listMenu = ArrayList<CartItem>()
    private lateinit var txtEmptyCart: TextView
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
        txtEmptyCart = view.findViewById(R.id.textEmptyCart)
        textTotal = view.findViewById(R.id.textTotalPrice)
        textTaxPrice = view.findViewById(R.id.textTaxPrice)
        textGrandTotal = view.findViewById(R.id.grandTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        // Mendapatkan data dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("user_pref", MODE_PRIVATE)
        // Mengambil userId dari SharedPreferences
        val userId = sharedPreferences.getInt("user_id", 0)
        val location = sharedPreferences.getString("location", "Location") ?: "Location"

        // Initialize RecyclerView
        val RVMenu = view.findViewById<RecyclerView>(R.id.rvProducts)
        val adapter = CartAdapter(listMenu, this@CartFragment, requireContext())
        RVMenu.layoutManager = LinearLayoutManager(context)
        RVMenu.adapter = adapter

        val itemAnimator = RVMenu.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        // Fetch cart items from API
        if (userId != null) {
            RetrofitClient.instance.getCartItems(userId).enqueue(object : Callback<ArrayList<CartItem>> {
                override fun onResponse(
                    call: Call<ArrayList<CartItem>>,
                    response: Response<ArrayList<CartItem>>
                ) {
                    if (isAdded) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                // Clear current items and add the updated items
                                listMenu.clear()
                                listMenu.addAll(body)

                                // Notify the adapter about data changes
                                RVMenu?.adapter?.notifyDataSetChanged()  // This will update the RecyclerView

                                // Handle empty cart view visibility
                                if (listMenu.isEmpty()) {
                                    txtEmptyCart.visibility = View.VISIBLE
                                    btnCheckout.isEnabled = false  // Disable the checkout button
                                } else {
                                    txtEmptyCart.visibility = View.GONE
                                    btnCheckout.isEnabled = true  // Enable the checkout button
                                }

                                // Recalculate totals
                                calculateTotals()
                            } else {
                                Log.e("CartFragment", "Response body is null")
                                txtEmptyCart.visibility = View.VISIBLE
                                btnCheckout.isEnabled = false  // Disable the checkout button if no items are in the cart
                            }
                        } else {
                            Log.e("CartFragment", "Response is not successful: ${response.code()} ${response.message()}")
                            txtEmptyCart.visibility = View.VISIBLE
                            btnCheckout.isEnabled = false  // Disable the checkout button if request fails
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<CartItem>>, t: Throwable) {
                    Log.e("CartFragment", "API call failed", t)
                    txtEmptyCart.visibility = View.VISIBLE
                    btnCheckout.isEnabled = false  // Disable the checkout button if request fails
                }
            })
        }


        btnCheckout.setOnClickListener {
            if (listMenu.isNotEmpty()) {  // Check if the cart is not empty
                val totalAmount = textGrandTotal.text.toString().replace("Rp ", "").replace(",", "").toDouble()

                // Membuat order baru
                val order = OrderRequest(
                    user_id = userId,
                    total = totalAmount,  // totalAmount sudah dihitung di UI
                    location = location,
                    order_date = System.currentTimeMillis()
                )

                // Mengirim request untuk membuat order
                RetrofitClient.instance.createOrder(order).enqueue(object : Callback<OrderResponse> {
                    override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                        if (response.isSuccessful) {
                            val orderId = response.body()?.order_id ?: return
                            Log.d("CartFragment", "Order created successfully with ID: $orderId")
                            // Passing the order_id and total to TransactionFragment using Fragment arguments
                            val transactionFragment = TransactionFragment()
                            val bundle = Bundle().apply {
                                putInt("order_id", orderId)
                                putInt("total", totalAmount.toInt())
                            }
                            transactionFragment.arguments = bundle

                            // Call HomeActivity method to update bottom navigation
                            activity?.let { activity ->
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
            } else {
                // Show a message if the cart is empty
                Log.d("CartFragment", "Cart is empty. Cannot checkout.")
            }
        }
    }


    private fun calculateTotals() {
        if (listMenu.isNotEmpty()) {
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
        } else {
            // If the list is empty, set the totals to Rp. 0
            textTotal.text = "Rp. 0"
            textTaxPrice.text = "Rp. 0"
            textGrandTotal.text = "Rp. 0"
        }
    }


    private fun formatCurrency(amount: Double): String {
        // Formatting the currency with two decimal places
        return String.format("%,.0f", amount) // Ensure that the amount is formatted correctly
    }

    // Implementing the interface method from CartAdapter
    override fun onTotalPriceChanged() {
        if (listMenu.isEmpty()) {
            txtEmptyCart.visibility = View.VISIBLE // Show "Keranjang Kosong" message
        } else {
            txtEmptyCart.visibility = View.GONE // Hide the "Keranjang Kosong" message
        }
        // Recalculate totals whenever quantity changes
        calculateTotals()
    }

}
