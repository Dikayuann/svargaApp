package com.example.svargaapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.squareup.picasso.Picasso
import com.example.svargaapp.response.paymentMethod.PaymentMethodResponse

class PaymentMethodAdapter(
    private val paymentMethods: List<PaymentMethodResponse>,
    private val onItemClick: (PaymentMethodResponse) -> Unit
) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentViewHolder>() {

    inner class PaymentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textPaymentQris)
        val textDescription: TextView = view.findViewById(R.id.textPaymentDesc)
        val imageIcon: ImageView = view.findViewById(R.id.paymentLogo)
        val textButton: TextView = view.findViewById(R.id.textButton)

        fun bind(paymentMethod: PaymentMethodResponse) {
            textName.text = paymentMethod.name_method
            textDescription.text = "Admin: Rp. ${paymentMethod.admin}"

            // Memuat gambar dengan Picasso
            var url = RetrofitClient.IMAGE_URL + paymentMethod.image
            Picasso.get().load(url).into(imageIcon)

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
        Log.d("RecyclerView", "Binding data: ${paymentMethod.name_method}")
    }

    override fun getItemCount(): Int = paymentMethods.size
}
