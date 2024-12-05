package com.example.svargaapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    private fun updateActiveIcon(activeImageView: ImageView, otherImageViews: List<ImageView>) {
        activeImageView.setColorFilter(ContextCompat.getColor(this, R.color.ecru))
        otherImageViews.forEach {
            it.setColorFilter(ContextCompat.getColor(this, R.color.white))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val imgMenu: ImageView = findViewById(R.id.imageView2)
        val imgCart: ImageView = findViewById(R.id.imageView3)
        val imgTransaction: ImageView = findViewById(R.id.imageView4)
        val imgHistory: ImageView = findViewById(R.id.imageView5)
        val imgProfile: ImageView = findViewById(R.id.imageView6)

        replaceFragment(MenuFragment())
        imgMenu.setColorFilter(ContextCompat.getColor(this, R.color.ecru))

        imgMenu.setOnClickListener {
            replaceFragment(MenuFragment())
            updateActiveIcon(imgMenu, listOf(imgCart, imgTransaction, imgHistory, imgProfile))
        }

        imgCart.setOnClickListener {
            replaceFragment(CartFragment())
            updateActiveIcon(imgCart, listOf(imgMenu, imgTransaction, imgHistory, imgProfile))
        }

        imgTransaction.setOnClickListener {
            replaceFragment(TransactionFragment())
            updateActiveIcon(imgTransaction, listOf(imgMenu, imgCart, imgHistory, imgProfile))
        }

        imgHistory.setOnClickListener {
            replaceFragment(HistoryFragment())
            updateActiveIcon(imgHistory, listOf(imgMenu, imgCart, imgTransaction, imgProfile))
        }

        imgProfile.setOnClickListener {
            replaceFragment(ProfileFragment())
            updateActiveIcon(imgProfile, listOf(imgMenu, imgCart, imgTransaction, imgHistory))
        }
    }
}
