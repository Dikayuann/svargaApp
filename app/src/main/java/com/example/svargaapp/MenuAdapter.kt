package com.example.svargaapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
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
        val textId: TextView = v.findViewById(R.id.textId)
        val textDescription: TextView = v.findViewById(R.id.textDescription)
        val textPrice: TextView = v.findViewById(R.id.textPrice)
        val menuImage: ImageView = v.findViewById(R.id.imageMenu)
        var cardMenu: CardView = v.findViewById(R.id.cardMenu)
        val context: Context? = v.context

        // Bind data dari MenuResponse ke tampilan (View)
        fun bind(response: MenuResponse) {
            // Ambil data dari response API
            val name = "${response.name}"
            val id = "${response.menu_id}"
            val subtitle = "${response.subtitle}"
            val description ="${response.description}"
            val price = "${response.price}"
            val category = "${response.category}"
            val picture = "${response.image}"

            // Masukkan data ke komponen-komponen tampilan
            textName.text = name
            textId.text = id
            textDescription.text = subtitle
            textPrice.text = price

            // Set gambar menu menggunakan Picasso dari URL
            var url = RetrofitClient.IMAGE_URL + picture
            Picasso.get().load(url).into(menuImage)

            // Set click listener for product detail
            cardMenu.setOnClickListener {
                val intent = Intent(context, ProductDetail::class.java)
                intent.putExtra("MENU_ID", id)
                intent.putExtra("MENU_NAME", name)
                intent.putExtra("MENU_SUBTITLE", subtitle)
                intent.putExtra("MENU_DESCRIPTION", description)
                intent.putExtra("MENU_PRICE", price)
                intent.putExtra("MENU_PICTURE", picture)
                context?.startActivity(intent)
            }
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
