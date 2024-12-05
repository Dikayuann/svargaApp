package com.example.svargaapp

import SliderAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MenuFragment : Fragment() {

    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable

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
        setCurrentIndicator(0)

        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = Runnable {
            if (viewPager.currentItem == images.size - 1) {
                viewPager.currentItem = 0
            } else {
                viewPager.currentItem++
            }
        }

        // Setup RecyclerView for menu items
        val menuList = listOf(
            MenuItem("Cappuccino", "With Milkshake", "Rp. 23.000", R.drawable.janu_profile),
            MenuItem("Midnight Berry", "Americano rasa buah blueberry", "Rp. 28.000", R.drawable.janu_profile),
            MenuItem("Coffee Java", "Kopi susu dengan gula aren", "Rp. 25.000", R.drawable.janu_profile),
            MenuItem("Vietnamese", "Kopi susu dengan krimer kental manis", "Rp. 25.000", R.drawable.janu_profile),
            MenuItem("Cappuccino", "With Milkshake", "Rp. 23.000", R.drawable.janu_profile),
            MenuItem("Midnight Berry", "Americano rasa buah blueberry", "Rp. 28.000", R.drawable.janu_profile),
            MenuItem("Coffee Java", "Kopi susu dengan gula aren", "Rp. 25.000", R.drawable.janu_profile),
            MenuItem("Vietnamese", "Kopi susu dengan krimer kental manis", "Rp. 25.000", R.drawable.janu_profile)
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MenuAdapter(menuList)

        // Mengatur LayoutManager menjadi GridLayoutManager dengan 2 kolom
        val layoutManager = GridLayoutManager(context, 2) // 2 kolom
        recyclerView.layoutManager = layoutManager


        // ViewPager2 callback and slider logic
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
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

    private fun setCurrentIndicator(index: Int) {
        val indicatorLayout = requireView().findViewById<LinearLayout>(R.id.indicatorLayout)
        val childCount = indicatorLayout.childCount
        for (i in 0 until childCount) {
            val indicator = indicatorLayout.getChildAt(i)
            if (i == index) {
                indicator.setBackgroundResource(R.drawable.indicator_active)
            } else {
                indicator.setBackgroundResource(R.drawable.indicator_inactive)
            }
        }
    }
}
