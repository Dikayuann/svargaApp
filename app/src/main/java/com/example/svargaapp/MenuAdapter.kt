package com.example.svargaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.response.menu.MenuResponse
import com.squareup.picasso.Picasso

// Adapter untuk RecyclerView yang digunakan untuk menampilkan menu
class MenuAdapter(
    private val listMenu: ArrayList<MenuResponse> // Menampung daftar menu
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    // ViewHolder untuk item menu
    inner class MenuViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // Inisialisasi komponen-komponen yang ada di layout item_menu
        val textName: TextView = v.findViewById(R.id.textName)
        val textDescription: TextView = v.findViewById(R.id.textDescription)
        val textPrice: TextView = v.findViewById(R.id.textPrice)
        val menuImage: ImageView = v.findViewById(R.id.imageMenu)
        val context: Context? = v.context

        // Bind data dari MenuResponse ke tampilan (View)
        fun bind(response: MenuResponse) {
            // Ambil data dari response API
            val name = "${response.name}"
            val description = "${response.description}"
            val price = "${response.price}"
            val category = "${response.category}"
            val picture = "${response.image}"

            // Masukkan data ke komponen-komponen tampilan
            textName.text = name
            textDescription.text = description
            textPrice.text = price

            // Set gambar menu menggunakan Picasso dari URL
            val url = "http://192.168.178.15/svargaApp/image/$picture"
            Picasso.get().load(url).into(menuImage)
        }
    }

    // Membuat ViewHolder untuk setiap item menu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    // Menyambungkan data ke ViewHolder berdasarkan posisi
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(listMenu[position])
    }

    // Fungsi untuk memperbarui daftar menu
    fun updateMenu(newMenuList: List<MenuResponse>) {
        listMenu.clear() // Hapus semua item sebelumnya
        listMenu.addAll(newMenuList) // Tambahkan daftar item baru
        notifyDataSetChanged() // Perbarui RecyclerView
    }

    // Mengembalikan jumlah item dalam daftar
    override fun getItemCount(): Int = listMenu.size
}
