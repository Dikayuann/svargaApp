package com.example.svargaapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.history.HistoryResponse
import com.example.svargaapp.response.menu.MenuResponse
import com.squareup.picasso.Picasso

class HistoryAdapter(
    private val historyList: ArrayList<HistoryResponse>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDateTime: TextView = itemView.findViewById(R.id.dateTime)
        val txtOrderId: TextView = itemView.findViewById(R.id.orderId)
        val txtStatus: ImageView = itemView.findViewById(R.id.btnStatus)
        val txtMenuPrice: TextView = itemView.findViewById(R.id.menuPrice)
        val txtNameHistory: TextView = itemView.findViewById(R.id.nameHistory)
        val txtNameHistoryOthers: TextView = itemView.findViewById(R.id.nameHistoryOthers)
        val ImageMenuHistory: ImageView = itemView.findViewById(R.id.imageMenuHistory)

        fun bind(response: HistoryResponse) {
            // Ambil data dari response API
            val dateTime = "${response.order_date}"
            val orderId = "${response.order_id}"
            val status = "${response.status}"
            val menuPrice = "Rp " + formatPrice(response.total.toDouble())
            val nameHistory = "${response.name}"
            val NameHistoryOthers = "${response.additional_items}"
            val imageMenu = "${response.image}"

            Log.d("HistoryAdapter", "Binding data for Order ID: $orderId")

            // Masukkan data ke komponen-komponen tampilan
            txtDateTime.text = dateTime
            txtOrderId.text = orderId
            txtMenuPrice.text = menuPrice
            txtNameHistory.text = nameHistory


            txtNameHistoryOthers.text = "+ " + NameHistoryOthers
            if (NameHistoryOthers.toInt() == 0) {
                txtNameHistoryOthers.visibility = View.GONE
            } else {
                txtNameHistoryOthers.visibility = View.VISIBLE
            }

            // Set gambar menu menggunakan Picasso dari URL
            var url = RetrofitClient.IMAGE_URL + imageMenu
            Picasso.get().load(url).into(ImageMenuHistory)

            when (status) {
                "Belum Bayar" -> {
                    txtStatus.setImageResource(R.drawable.baseline_error_outline_15) // Ganti dengan ikon "Belum Bayar"
                }

                "Dalam Proses" -> {
                    txtStatus.setImageResource(R.drawable.baseline_hourglass_empty_15) // Ganti dengan ikon "Dalam Proses"
                }

                "Selesai" -> {
                    txtStatus.setImageResource(R.drawable.baseline_check_15) // Ganti dengan ikon "Selesai"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    private fun formatPrice(amount: Double): String {
        // Formatting the currency with two decimal places
        return String.format("%,.0f", amount) // Ensure that the amount is formatted correctly
    }
}
