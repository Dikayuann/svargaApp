package com.example.svargaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private var menuList: MutableList<MenuItem>,
    private val onAddToCart: (MenuItem) -> Unit // Callback untuk aksi tambah ke keranjang
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.name.text = menuItem.name
        holder.description.text = menuItem.description
        holder.price.text = menuItem.price
        holder.image.setImageResource(menuItem.image)
    }

    override fun getItemCount(): Int = menuList.size

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textName)
        val description: TextView = itemView.findViewById(R.id.textDescription)
        val price: TextView = itemView.findViewById(R.id.textPrice)
        val image: ImageView = itemView.findViewById(R.id.imageMenu)
    }
}
