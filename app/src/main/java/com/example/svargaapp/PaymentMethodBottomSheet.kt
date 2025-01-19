package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.svargaapp.response.paymentMethod.PaymentMethodResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentMethodBottomSheet(
    private val onPaymentSelected: (PaymentMethodResponse) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("BottomSheet", "PaymentMethodBottomSheet opened")

        val rvPayment: RecyclerView = view.findViewById(R.id.recyclerViewPayment)
        rvPayment.layoutManager = LinearLayoutManager(context)

        // Memuat data menggunakan Retrofit
        RetrofitClient.instance.getPaymentMethods().enqueue(object : Callback<List<PaymentMethodResponse>> {
            override fun onResponse(
                call: Call<List<PaymentMethodResponse>>,
                response: Response<List<PaymentMethodResponse>>
            ) {
                if (response.isSuccessful) {
                    val paymentMethodsResponse = response.body()
                    if (paymentMethodsResponse != null && paymentMethodsResponse.isNotEmpty()) {
                        // Set adapter dengan data yang diterima
                        val adapter = PaymentMethodAdapter(paymentMethodsResponse) { selectedPayment ->
                            Log.d("BottomSheet", "Selected payment: ${selectedPayment.name_method}")
                            onPaymentSelected(selectedPayment)
                            dismiss() // Tutup Bottom Sheet setelah memilih
                        }
                        rvPayment.adapter = adapter
                    } else {
                        Toast.makeText(context, "No payment methods available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load payment methods", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<PaymentMethodResponse>>, t: Throwable) {
                Log.e("Retrofit", "Error: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        val closeButton: ImageButton = view.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            Log.d("BottomSheet", "Bottom sheet closed")
            dismiss()
        }
    }
}
