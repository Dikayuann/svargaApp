package com.example.svargaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.discon.DiscountResponse
import com.example.svargaapp.response.payment.PaymentRequest
import com.example.svargaapp.response.payment.PaymentResponse
import com.example.svargaapp.response.paymentMethod.PaymentMethodResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionFragment : Fragment() {

    // Variables for order ID and total amount
    private var orderId: Int? = null
    private var total: Int? = null

    // Request code for location change
    private val LOCATION_REQUEST_CODE = 1001

    // UI components for location, payment, and discount info
    private lateinit var location: TextView
    private lateinit var btnChangeLocation: TextView
    private lateinit var paymentName: TextView
    private lateinit var paymentAdmin: TextView
    private lateinit var paymentLogo: ImageView
    private lateinit var discountName: TextView
    private lateinit var discountExpired: TextView
    private lateinit var discountLogo: ImageView
    private lateinit var txtNoCheckout: TextView
    private lateinit var txtTotal: TextView
    private lateinit var txtDiscountValue: TextView
    private lateinit var txtTax: TextView
    private lateinit var txtAdminFee: TextView
    private lateinit var txtGrandTotal: TextView
    private lateinit var btnBayar: TextView

    // Lists for payment methods and discount methods
    private var paymentMethodsList: List<PaymentMethodResponse> = emptyList()
    private var discountList: List<DiscountResponse> = emptyList()

    // Flags for delivery and takeaway options
    private var isDelivery: Boolean = false
    private var isTakeAway: Boolean = false

    private var grandTotal: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        txtNoCheckout = view.findViewById(R.id.txtNoCheckout)
        txtDiscountValue = view.findViewById(R.id.textDisconPrice)
        txtTax = view.findViewById(R.id.textTaxPrice)
        txtAdminFee = view.findViewById(R.id.textAdminPrice)
        txtGrandTotal = view.findViewById(R.id.grandTotal)

        // Retrieve arguments for order_id and total
        orderId = arguments?.getInt("order_id")
        total = arguments?.getInt("total")

        // Initialize total TextView
        txtTotal = view.findViewById(R.id.textTotalPrice)
        txtTotal.text = "Rp. ${"%,d".format(total)}"



        // Widgets for Address, Delivery, Takeaway, Promo, Payment
        val widgetAddress: View = view.findViewById(R.id.widgetAddress)
        val widgetDelivery: View = view.findViewById(R.id.widgetDelivery)
        val widgetTakeAway: View = view.findViewById(R.id.widgetTakeAway)
        val widgetPromo: View = view.findViewById(R.id.widgetPromo)
        val widgetPayment: View = view.findViewById(R.id.widgetPayment)

        // Handle the visibility of widgets based on orderId
        if (orderId == null) {
            txtNoCheckout.visibility = View.VISIBLE
            widgetAddress.visibility = View.GONE
            widgetDelivery.visibility = View.GONE
            widgetTakeAway.visibility = View.GONE
            widgetPromo.visibility = View.GONE
            widgetPayment.visibility = View.GONE
        } else {
            txtNoCheckout.visibility = View.GONE
            widgetAddress.visibility = View.VISIBLE
            widgetDelivery.visibility = View.VISIBLE
            widgetTakeAway.visibility = View.VISIBLE
            widgetPromo.visibility = View.VISIBLE
            widgetPayment.visibility = View.VISIBLE
            loadPaymentMethods()
            loadDiscountMethods()
        }

        // Initialize location TextView
        location = view.findViewById(R.id.textAddressStreet)
        location.text = LoginActivity.location

        // Button to change location
        btnChangeLocation = view.findViewById(R.id.btnChangeLocation)
        btnChangeLocation.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, LOCATION_REQUEST_CODE)
        }

        // Initialize payment method views
        paymentName = view.findViewById(R.id.textPaymentQris)
        paymentAdmin = view.findViewById(R.id.textPaymentDesc)
        paymentLogo = view.findViewById(R.id.paymentLogo)

        // Initialize discount views
        discountName = view.findViewById(R.id.textDiscount)
        discountExpired = view.findViewById(R.id.textDiscountDesc)
        discountLogo = view.findViewById(R.id.promoLogo)

        // Button for selecting payment method
        val btnCheckPayment: ImageView = view.findViewById(R.id.btnOtherPayment)
        btnCheckPayment.setOnClickListener {
            openPaymentBottomSheet()
        }

        // Button for selecting discount
        val btnCheckDiscount: ImageView = view.findViewById(R.id.btnOtherDiscount)
        btnCheckDiscount.setOnClickListener {
            openDiscountBottomSheet()
        }

        // Event listeners for Delivery and Takeaway
        val deliveryOption: View = view.findViewById(R.id.widgetDelivery)
        val takeawayOption: View = view.findViewById(R.id.widgetTakeAway)

        deliveryOption.setOnClickListener {
            isDelivery = true
            isTakeAway = false
            updateTotal()  // Recalculate total when delivery is selected
        }

        takeawayOption.setOnClickListener {
            isTakeAway = true
            isDelivery = false
            updateTotal()  // Recalculate total when takeaway is selected
        }

        // Initialize btnBayar
        btnBayar = view.findViewById(R.id.btnBayar)

        // Button click listener for processing payment
        btnBayar.setOnClickListener {
            // Check if all necessary fields are selected
            val selectedPaymentMethod = paymentMethodsList.find { it.name_method == paymentName.text.toString() }
            val selectedDiscount = discountList.find { it.name_discount == discountName.text.toString() }

            if (selectedPaymentMethod != null && selectedDiscount != null && orderId != null) {
                // Call processPayment function with grandTotal calculated earlier
                processPayment(orderId!!, selectedDiscount.id_discount, selectedPaymentMethod.id_method, grandTotal!!)
            } else {
                Toast.makeText(context, "Please select payment method and discount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to calculate and update totals
    private fun updateTotal() {
        if (total != null) {
            var discountAmount = 0
            var adminFee = 0
            var deliveryCost = 0

            // Handle Delivery or Takeaway cost
            if (isDelivery) {
                deliveryCost = 5000  // Example delivery cost
            } else if (isTakeAway) {
                deliveryCost = 0  // Example takeaway cost
            }

            // Apply discount if available
            val discount = discountList.find { it.name_discount == discountName.text.toString() }
            if (discount != null) {
                // Use the discount percentage (e.g., 5%) and calculate the discount amount
                val discountValue = discount.value.toIntOrNull() ?: 0
                Log.d("UpdateTotal", "Discount Value: Rp. ${"%,d".format(discountValue)}")

                // Calculate the discount amount based on the total
                discountAmount = ((discountValue * total!!).toInt() / 100)
                Log.d("UpdateTotal", "Discount Amount: Rp. ${"%,d".format(discountAmount)}")
            } else {
                Log.d("UpdateTotal", "No discount applied.")
            }


            // Apply admin fee based on the selected payment method
            val selectedPaymentMethod = paymentMethodsList.find { it.name_method == paymentName.text.toString() }
            if (selectedPaymentMethod != null) {
                val admin = selectedPaymentMethod.admin.toIntOrNull() ?: 0
                Log.d("UpdateTotal", "Admin Fee: Rp. ${"%,d".format(admin)}")
                adminFee = admin
                Log.d("UpdateTotal", "Admin Fee: Rp. ${"%,d".format(adminFee)}")
            } else {
                Log.d("UpdateTotal", "No admin fee applied.")
            }


            // Calculate tax (12%)
            val tax = (total!! * 0.12).toInt()
            Log.d("UpdateTotal", "Tax: Rp. ${"%,d".format(tax)}")

            // Calculate grand total
            grandTotal = total!! - discountAmount + tax + adminFee + deliveryCost
            Log.d("UpdateTotal", "Grand Total: Rp. ${"%,d".format(grandTotal)}")

            // Update UI with calculated values
            txtDiscountValue.text = "Rp. ${"%,d".format(discountAmount)}"
            txtTax.text = "Rp. ${"%,d".format(tax)}"
            txtAdminFee.text = "Rp. ${"%,d".format(adminFee)}"
            txtGrandTotal.text = "Rp. ${"%,d".format(grandTotal)}"
        }
    }

    // Function to send payment data to the backend
    private fun processPayment(orderId: Int, discountId: String, paymentMethodId: Int, grandTotal: Int) {
        val paymentRequest = PaymentRequest(
            order_id = orderId,
            id_discount = discountId.toString(),
            id_method = paymentMethodId,
            payment_status = "berhasil",
            payment_date = System.currentTimeMillis(),
            total = grandTotal,
            status = "dalam proses"
        )

        // Send the data to the backend via a POST request (Use your Retrofit or any network library)
        RetrofitClient.instance.createPayment(paymentRequest).enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                if (response.isSuccessful) {
                    // Jika berhasil, tangani responsenya di sini
                    val paymentResponse = response.body()
                    paymentResponse?.let {
                        // Tampilkan hasil atau lakukan sesuatu berdasarkan response
                        Toast.makeText(context, "Payment successfully processed", Toast.LENGTH_SHORT).show()
                        Log.d("Payment", "Payment ID: ${it.payment_id}")
                    }
                } else {
                    Toast.makeText(context, "Failed to process payment", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                // Tangani kesalahan jika terjadi
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // Function to load payment methods
    private fun loadPaymentMethods() {
        RetrofitClient.instance.getPaymentMethods().enqueue(object : Callback<List<PaymentMethodResponse>> {
            override fun onResponse(
                call: Call<List<PaymentMethodResponse>>,
                response: Response<List<PaymentMethodResponse>>
            ) {
                if (response.isSuccessful) {
                    paymentMethodsList = response.body() ?: emptyList()

                    if (paymentMethodsList.isNotEmpty()) {
                        val firstPaymentMethod = paymentMethodsList[0]
                        updatePaymentMethod(firstPaymentMethod)
                    }

                    // After loading payment methods, update the total
                    updateTotal()
                } else {
                    Toast.makeText(context, "Failed to load payment methods", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PaymentMethodResponse>>, t: Throwable) {
                Log.e("Retrofit", "Error: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to load discount methods
    private fun loadDiscountMethods() {
        RetrofitClient.instance.getDiscount().enqueue(object : Callback<List<DiscountResponse>> {
            override fun onResponse(
                call: Call<List<DiscountResponse>>,
                response: Response<List<DiscountResponse>>
            ) {
                if (response.isSuccessful) {
                    discountList = response.body() ?: emptyList()

                    if (discountList.isNotEmpty()) {
                        val firstDiscountMethod = discountList[0]
                        updateDiscountMethod(firstDiscountMethod)
                    }

                    // After loading discount methods, update the total
                    updateTotal()
                } else {
                    Toast.makeText(context, "Failed to load discount methods", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DiscountResponse>>, t: Throwable) {
                Log.e("Retrofit", "Error: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to update payment method details in UI
    private fun updatePaymentMethod(paymentMethod: PaymentMethodResponse) {
        paymentName.text = paymentMethod.name_method
        paymentAdmin.text = "Admin: Rp. ${paymentMethod.admin}"

        val url = RetrofitClient.IMAGE_URL + paymentMethod.image
        Picasso.get().load(url).into(paymentLogo)

        // Recalculate the total when payment method is selected
        updateTotal()
    }

    // Function to update discount method details in UI
    private fun updateDiscountMethod(discountResponse: DiscountResponse) {
        discountName.text = discountResponse.name_discount
        discountExpired.text = "Berlaku s/d ${discountResponse.expired}"

        val url = RetrofitClient.IMAGE_URL + discountResponse.image
        Picasso.get().load(url).into(discountLogo)

        // Recalculate the total when discount is selected
        updateTotal()
    }

    // Function to open payment method bottom sheet
    private fun openPaymentBottomSheet() {
        if (paymentMethodsList.isNotEmpty()) {
            val bottomSheet = PaymentMethodBottomSheet { selectedPayment ->
                updatePaymentMethod(selectedPayment)
            }
            bottomSheet.show(parentFragmentManager, "PaymentMethodBottomSheet")
        } else {
            Toast.makeText(context, "No payment methods available", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to open discount bottom sheet
    private fun openDiscountBottomSheet() {
        if (discountList.isNotEmpty()) {
            val bottomSheet = DiscountFragment { selectedDiscount ->
                updateDiscountMethod(selectedDiscount)
            }
            bottomSheet.show(parentFragmentManager, "DiscountBottomSheet")
        } else {
            Toast.makeText(context, "No discount methods available", Toast.LENGTH_SHORT).show()
        }
    }
}
