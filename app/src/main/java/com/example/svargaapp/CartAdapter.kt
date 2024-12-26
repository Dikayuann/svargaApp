package com.example.svargaapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.cart.CartItem
import com.squareup.picasso.Picasso

class CartAdapter(private val productList: ArrayList<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder yang mengikat item dalam RecyclerView
    inner class CartViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // Inisialisasi komponen-komponen yang ada di layout item_cart
        val id: TextView = v.findViewById(R.id.menuId)
        val textName: TextView = v.findViewById(R.id.menuName)
        val textPrice: TextView = v.findViewById(R.id.menuPrice)
        val textQuantity: TextView = v.findViewById(R.id.Quantity)
        val CartImage: ImageView = v.findViewById(R.id.imageMenuCart)
        val btnIncrease: ImageView = v.findViewById(R.id.btnIncrease)
        val btnDecrease: ImageView = v.findViewById(R.id.btnDecrease)
        val context: Context? = v.context

        // Fungsi bind untuk mengikat data dengan tampilan
        fun bind(response: CartItem) {
            // Bind data ke item
            val cartId = "${response.cart_id}"
            val name = response.name ?: "Nama Tidak Tersedia"
            val quantity = "${response.quantity}"
            val price = "Rp ${response.price}"
            val picture = "${response.image}"

            // Masukkan data ke komponen-komponen tampilan
            id.text = cartId
            textName.text = name
            textPrice.text = price
            textQuantity.text = quantity

            // Set gambar menu menggunakan Picasso dari URL
            val url = RetrofitClient.IMAGE_URL + picture
            Picasso.get().load(url).into(CartImage)

            // Set listener untuk tombol tambah dan kurangi kuantitas
            btnIncrease.setOnClickListener {
                // Cek apakah item sudah ada di dalam daftar
                val index = productList.indexOfFirst { it.menu_id == response.menu_id }
                if (index != -1) {
                    val updatedItem = productList[index]
                    updatedItem.quantity++ // Tambah kuantitas
                    productList[index] = updatedItem // Update item dalam list
                    textQuantity.text = updatedItem.quantity.toString() // Update tampilan
                    notifyItemChanged(index) // Update tampilan item yang diubah
                }
            }

            btnDecrease.setOnClickListener {
                if (response.quantity > 1) {
                    response.quantity-- // Kurangi kuantitas
                    textQuantity.text = response.quantity.toString() // Update tampilan
                    notifyItemChanged(adapterPosition) // Update UI
                } else {
                    // Hapus item jika kuantitas menjadi 0
                    productList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    notifyItemRangeChanged(adapterPosition, itemCount)
                }
            }
        }
    }

    // Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    // Mengikat data item ke ViewHolder
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product) // Panggil fungsi bind untuk mengikat data
    }

    // Menghitung jumlah item di dalam adapter
    override fun getItemCount(): Int = productList.size

    // Fungsi untuk update data produk
    fun updateData(newProductList: ArrayList<CartItem>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }
}

