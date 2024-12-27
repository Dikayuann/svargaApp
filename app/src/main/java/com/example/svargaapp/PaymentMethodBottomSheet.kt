package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentMethodBottomSheet(
    private val onPaymentSelected: (PaymentMethodModel) -> Unit
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

        val paymentMethods = listOf(
            PaymentMethodModel("Dana", "Admin: Rp. 0", R.drawable.qris),
            PaymentMethodModel("Gopay", "Admin: Rp. 1.000", R.drawable.janu_profile),
            PaymentMethodModel("Ovo", "Admin: Rp. 1.000", R.drawable.qris)
        )

        // PaymentMethodBottomSheet
        val adapter = PaymentMethodAdapter(paymentMethods) { selectedPayment ->
            Log.d("BottomSheet", "Selected payment: ${selectedPayment.name}")
            onPaymentSelected(selectedPayment)
            dismiss() // Tutup Bottom Sheet setelah memilih
        }

        rvPayment.adapter = adapter

        val closeButton: ImageButton = view.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            Log.d("BottomSheet", "Bottom sheet closed")
            dismiss()
        }
    }
}
