package com.example.svargaapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class LogoutDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout item_logout.xml
        val view = inflater.inflate(R.layout.item_logout, container, false)

        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)

        // Tombol Cancel - Tutup dialog tanpa logout
        btnCancel.setOnClickListener {
            dismiss() // Menutup dialog tanpa logout
        }

        // Tombol Logout - Hapus data dan redirect ke login
        btnLogout.setOnClickListener {
            // Menghapus data login di SharedPreferences
            val sharedPreferences = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()  // Menghapus semua data

            // Arahkan ke LoginActivity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Menutup aktivitas saat logout
        }

        return view
    }
}
