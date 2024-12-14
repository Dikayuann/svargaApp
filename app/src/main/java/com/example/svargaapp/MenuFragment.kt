package com.example.svargaapp

import SliderAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.svargaapp.CartRepository.CartRepository

class MenuFragment : Fragment() {

    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable
    private val buttonList = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Slider setup
        val images = listOf(
            R.drawable.promo_svarga1,
            R.drawable.promo_svarga2,
            R.drawable.promo_svarga3,
            R.drawable.promo_svarga4
        )
        val sliderAdapter = SliderAdapter(images)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = sliderAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // Timer untuk slider
        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = Runnable {
            if (viewPager.currentItem == images.size - 1) {
                viewPager.currentItem = 0
            } else {
                viewPager.currentItem++
            }
        }

        // Daftar item menu
        val menuList = listOf(
            MenuItem("Cappuccino", "With Milkshake", "23000", "Hot", R.drawable.menu1),
            MenuItem("Midnight Berry", "Americano rasa buah blueberry", "28000", "Cold", R.drawable.menu2),
            MenuItem("Coffee Java", "Kopi susu dengan gula aren", "25000", "Hot", R.drawable.menu3),
            MenuItem("Vietnamese", "Kopi susu dengan krimer kental manis", "25000", "Cold", R.drawable.menu4)
        )

        // RecyclerView dan MenuAdapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val menuAdapter = MenuAdapter(menuList.toMutableList()) { selectedItem ->
            // Tambahkan produk ke keranjang
            val product = Product(
                name = selectedItem.name,
                price = selectedItem.price.toDouble(),
                quantity = 1,
                imageResourceId = selectedItem.image
            )
            CartRepository.addToCart(product)
            Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            Log.d("MenuFragment", "Product added to cart: $product")
        }
        recyclerView.adapter = menuAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // ViewPager2 callback
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 5000)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 5000)
    }
}
