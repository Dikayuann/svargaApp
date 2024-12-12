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
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MenuFragment : Fragment() {

    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable
    private lateinit var btnAll: TextView
    private lateinit var btnHot: TextView
    private lateinit var btnCold: TextView
    private lateinit var btnOthers: TextView
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
            MenuItem("Cappuccino", "With Milkshake", "Rp. 23.000", "Hot", R.drawable.menu1),
            MenuItem("Midnight Berry", "Americano rasa buah blueberry", "Rp. 28.000", "Cold", R.drawable.menu2),
            MenuItem("Coffee Java", "Kopi susu dengan gula aren", "Rp. 25.000", "Hot", R.drawable.menu3),
            MenuItem("Vietnamese", "Kopi susu dengan krimer kental manis", "Rp. 25.000", "Cold", R.drawable.menu4)
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val menuAdapter = MenuAdapter(menuList.toMutableList())
        recyclerView.adapter = menuAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // Setup filter buttons
        btnAll = view.findViewById(R.id.btnAll)
        btnHot = view.findViewById(R.id.btnHot)
        btnCold = view.findViewById(R.id.btnCold)
        btnOthers = view.findViewById(R.id.btnOthers)

        buttonList.addAll(listOf(btnAll, btnHot, btnCold, btnOthers))

        btnAll.setOnClickListener {
            // Tampilkan semua item
            menuAdapter.updateMenu(menuList)
            setActiveButton(btnAll)
        }

        btnHot.setOnClickListener {
            // Filter item kategori "Hot"
            val filteredList = menuList.filter { it.category == "Hot" }
            menuAdapter.updateMenu(filteredList)
            setActiveButton(btnHot)
        }

        btnCold.setOnClickListener {
            // Filter item kategori "Cold"
            val filteredList = menuList.filter { it.category == "Cold" }
            menuAdapter.updateMenu(filteredList)
            setActiveButton(btnCold)
        }

        btnOthers.setOnClickListener {
            // Filter item kategori "Others"
            val filteredList = menuList.filter { it.category == "Others" }
            menuAdapter.updateMenu(filteredList)
            setActiveButton(btnOthers)
        }

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

    private fun setActiveButton(activeButton: TextView) {
        buttonList.forEach { button ->
            if (button == activeButton) {
                button.setBackgroundResource(R.drawable.button_category_active)  // Background active
                button.setTextColor(resources.getColor(R.color.white, null))      // Change text color to white or another color for active state
            } else {
                button.setBackgroundResource(R.drawable.button_category_inactive)  // Background inactive
                button.setTextColor(resources.getColor(R.color.black, null))      // Change text color to black or another color for inactive state
            }
        }
    }
}
