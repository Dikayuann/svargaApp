package com.example.svargaapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PaymentMethodAdapter(
    private val paymentMethods: List<PaymentMethodModel>,
    private val onItemClick: (PaymentMethodModel) -> Unit) :
    RecyclerView.Adapter<PaymentMethodAdapter.PaymentViewHolder>() {

    inner class PaymentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textPaymentQris)
        val textDescription: TextView = view.findViewById(R.id.textPaymentDesc)
        val imageIcon: ImageView = view.findViewById(R.id.paymentLogo)
        val textButton: TextView = view.findViewById(R.id.textButton)

        fun bind(paymentMethod: PaymentMethodModel) {
            textName.text = paymentMethod.name
            textDescription.text = paymentMethod.description
            imageIcon.setImageResource(paymentMethod.image)

            // Di dalam PaymentMethodAdapter
            textButton.setOnClickListener {
                onItemClick(paymentMethod)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_method, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val paymentMethod = paymentMethods[position]
        holder.bind(paymentMethod)
        Log.d("RecyclerView", "Binding data: ${paymentMethod.name}")
    }

    override fun getItemCount(): Int = paymentMethods.size
}
