package com.example.svargaapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {
    // function for replace fragment
    private fun  replaceFragment(fragment:Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTrx = fragmentManager.beginTransaction()
        fragmentTrx.replace(R.id.fragmentContainerView, fragment)
        fragmentTrx.commit()
    }

    private fun updateActiveIcon(activeImageView: ImageView, otherImageViews: List<ImageView>) {
        // Set active color
        activeImageView.setColorFilter(ContextCompat.getColor(this, R.color.ecru))

        // Set inactive color for other icons
        otherImageViews.forEach {
            it.setColorFilter(ContextCompat.getColor(this, R.color.white))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //instance
        val imgMenu:ImageView = findViewById(R.id.imageView2)
        val imgCart:ImageView = findViewById(R.id.imageView3)
        val imgTransaction:ImageView = findViewById(R.id.imageView4)
        val imgHistory:ImageView = findViewById(R.id.imageView5)
        val imgProfile:ImageView = findViewById(R.id.imageView6)

        // Set default fragment
        replaceFragment(MenuFragment())
        imgMenu.setColorFilter(ContextCompat.getColor(this, R.color.ecru))

        //event imgMenu click
        imgMenu.setOnClickListener{
            replaceFragment(MenuFragment())
            updateActiveIcon(imgMenu, listOf(imgCart, imgTransaction, imgHistory, imgProfile))
        }

        //event imgCart click
        imgCart.setOnClickListener{
            replaceFragment(CartFragment())
            updateActiveIcon(imgCart, listOf(imgMenu, imgTransaction, imgHistory, imgProfile))
        }

        //event imgTransaction click
        imgTransaction.setOnClickListener{
            replaceFragment(TransactionFragment())
            updateActiveIcon(imgTransaction, listOf(imgMenu, imgCart, imgHistory, imgProfile))
        }

        //event imgHistory click
        imgHistory.setOnClickListener{
            replaceFragment(HistoryFragment())
            updateActiveIcon(imgHistory, listOf(imgMenu, imgCart, imgTransaction, imgProfile))
        }

        //event imgProfile click
        imgProfile.setOnClickListener{
            replaceFragment(ProfileFragment())
            updateActiveIcon(imgProfile, listOf(imgMenu, imgCart, imgTransaction, imgHistory))
        }

    }
}