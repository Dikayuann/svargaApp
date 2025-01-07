package com.example.svargaapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.discon.DiscountResponse
import com.squareup.picasso.Picasso

class DiscountAdapter(
    private val discountMethods: List<DiscountResponse>,
    private val onItemClick: (DiscountResponse) -> Unit
) : RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>() {

    inner class DiscountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textNameDiscount)  // ID yang sesuai
        val textDescription: TextView = view.findViewById(R.id.textDiscountDesc)  // ID yang sesuai
        val imageIcon: ImageView = view.findViewById(R.id.discountLogo)  // ID yang sesuai
        val textButton: TextView = view.findViewById(R.id.textButton)  // ID yang sesuai

        fun bind(discountMethod: DiscountResponse) {
            textName.text = discountMethod.name_discount
            textDescription.text = "Berlaku s/d ${discountMethod.expired}"

            // Memuat gambar dengan Picasso
            val url = RetrofitClient.IMAGE_URL + discountMethod.image
            Picasso.get().load(url).into(imageIcon)

            textButton.setOnClickListener {
                onItemClick(discountMethod)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discount, parent, false)
        return DiscountViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        val discountMethod = discountMethods[position]
        holder.bind(discountMethod)
        Log.d("RecyclerView", "Binding data: ${discountMethod.name_discount}")
    }

    override fun getItemCount(): Int = discountMethods.size
}
