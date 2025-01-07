package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.discon.DiscountResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscountFragment(
    private val onDiscountSelected: (DiscountResponse) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("BottomSheet", "DiscountBottomSheet opened")

        val rvDiscount: RecyclerView = view.findViewById(R.id.recyclerViewDiscount)
        rvDiscount.layoutManager = LinearLayoutManager(context)

        // Memuat data menggunakan Retrofit
        RetrofitClient.instance.getDiscount().enqueue(object :
            Callback<List<DiscountResponse>> {
            override fun onResponse(
                call: Call<List<DiscountResponse>>,
                response: Response<List<DiscountResponse>>
            ) {
                if (response.isSuccessful) {
                    val discountList = response.body()
                    if (discountList != null && discountList.isNotEmpty()) {
                        // Set adapter dengan data yang diterima
                        val adapter = DiscountAdapter(discountList) { selectedDiscount ->
                            Log.d("BottomSheet", "Selected discount: ${selectedDiscount.name_discount}")
                            onDiscountSelected(selectedDiscount)
                            dismiss() // Tutup Bottom Sheet setelah memilih
                        }
                        rvDiscount.adapter = adapter
                    } else {
                        Toast.makeText(context, "No discount methods available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load discount methods", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DiscountResponse>>, t: Throwable) {
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
