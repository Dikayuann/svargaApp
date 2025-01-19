package com.example.svargaapp


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.svargaapp.client.RetrofitClient
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("user_pref", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "Guest")
        val name = sharedPreferences.getString("name", "No Name")
        val fotoProfilePath = sharedPreferences.getString("foto_profile", "")

        val textProfileName: TextView = view.findViewById(R.id.textProfileName)
        val textProfileUsername: TextView = view.findViewById(R.id.textProfileEmail)
        val fotoProfile: ImageView = view.findViewById(R.id.imageViewProfile)

        textProfileName.text = name
        textProfileUsername.text = email

        // Mengatur gambar profil jika ada
        if (!fotoProfilePath.isNullOrEmpty()) {
            val url = RetrofitClient.IMAGE_URL + fotoProfilePath
            Picasso.get().load(url).into(fotoProfile)
        }

        val profiledt: LinearLayout = view.findViewById(R.id.privateInformation)

        profiledt.setOnClickListener {
            // Mengarahkan ke ProfileDetail Activity
            val intent = Intent(requireActivity(), ProfileDetail::class.java)
            startActivity(intent)
        }

        val profileEdit: LinearLayout = view.findViewById(R.id.editProfile)

        profileEdit.setOnClickListener {
            // Mengarahkan ke ProfileDetail Activity
            val intent = Intent(requireActivity(), ProfileEdit::class.java)
            startActivity(intent)
        }

        val logoutButton: LinearLayout = view.findViewById(R.id.logout)

        logoutButton.setOnClickListener {
            // Tampilkan dialog konfirmasi logout
            showLogoutDialog()
        }
        }
            // Menampilkan dialog konfirmasi logout
            private fun showLogoutDialog() {
                val logoutDialog = LogoutDialogFragment()
                logoutDialog.show(childFragmentManager, "LogoutDialog")
            }
    }

