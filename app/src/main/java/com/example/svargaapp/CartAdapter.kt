package com.example.svargaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private val productList: MutableList<Product>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = productList[position]
        holder.name.text = product.name
        holder.price.text = "Rp ${product.price}"
        holder.quantity.text = product.quantity.toString()
        holder.ivProductImage.setImageResource(product.imageResourceId)

        // Increment quantity
        holder.btnIncrease.setOnClickListener {
            product.quantity++
            holder.quantity.text = product.quantity.toString()
            notifyItemChanged(position) // Update UI
        }

        // Decrement quantity (but not below 1)
        holder.btnDecrease.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.quantity.text = product.quantity.toString()
                notifyItemChanged(position) // Update UI
            } else {
                // Remove item if quantity drops to 0
                productList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val btnIncrease: TextView = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: TextView = itemView.findViewById(R.id.btnDecrease)
    }

    // Update data when new items are added
    fun updateData(newProductList: List<Product>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }
}
