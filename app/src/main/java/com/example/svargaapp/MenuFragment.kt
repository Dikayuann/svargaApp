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
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.menu.MenuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment() {

    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable
    private lateinit var btnAll: TextView
    private lateinit var btnHot: TextView
    private lateinit var btnCold: TextView
    private lateinit var btnOthers: TextView
    private val buttonList = mutableListOf<TextView>()

    private val listMenu = ArrayList<MenuResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup for the image slider
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

        // Set up a timer for the slider to change images every 5 seconds
        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = Runnable {
            if (viewPager.currentItem == images.size - 1) {
                viewPager.currentItem = 0
            } else {
                viewPager.currentItem++
            }
        }

        // RecyclerView setup
        val RVMenu: RecyclerView = view.findViewById(R.id.recyclerView)
        RVMenu.layoutManager = GridLayoutManager(activity, 2)

        // Fetch menu data from API
        RetrofitClient.instance.getMenu().enqueue(
            object : Callback<ArrayList<MenuResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<MenuResponse>>,
                    response: Response<ArrayList<MenuResponse>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            listMenu.clear()
                            listMenu.addAll(body)
                            val adapter = MenuAdapter(listMenu)
                            RVMenu.adapter = adapter
                        } else {
                            Log.e("MenuFragment", "Response body is null")
                        }
                    } else {
                        Log.e("MenuFragment", "Response is not successful: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<MenuResponse>>, t: Throwable) {
                    Log.e("MenuFragment", "API call failed", t)
                }
            }
        )

        // Filter buttons setup
        btnAll = view.findViewById(R.id.btnAll)
        btnHot = view.findViewById(R.id.btnHot)
        btnCold = view.findViewById(R.id.btnCold)
        btnOthers = view.findViewById(R.id.btnOthers)

        buttonList.addAll(listOf(btnAll, btnHot, btnCold, btnOthers))

        // Set up click listeners for filter buttons
        btnAll.setOnClickListener {
            // Show all items
            setActiveButton(btnAll)
        }

        btnHot.setOnClickListener {
            // Filter items by category "Hot"
            val filteredList = listMenu.filter { it.category == "Hot" }
            setActiveButton(btnHot)
        }

        btnCold.setOnClickListener {
            // Filter items by category "Cold"
            val filteredList = listMenu.filter { it.category == "Cold" }
            setActiveButton(btnCold)
        }

        btnOthers.setOnClickListener {
            // Filter items by category "Others"
            val filteredList = listMenu.filter { it.category == "Others" }
            setActiveButton(btnOthers)
        }

        // ViewPager2 callback to reset timer when page is changed
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 5000)
                setCurrentIndicator(position)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable) // Stop slider when fragment is paused
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 5000) // Restart slider when fragment is resumed
    }

    // Set the indicator of the current slide in the slider
    private fun setCurrentIndicator(index: Int) {
        val indicatorLayout = requireView().findViewById<LinearLayout>(R.id.indicatorLayout)
        val childCount = indicatorLayout.childCount
        for (i in 0 until childCount) {
            val indicator = indicatorLayout.getChildAt(i)
            if (i == index) {
                indicator.setBackgroundResource(R.drawable.indicator_active) // Active indicator
            } else {
                indicator.setBackgroundResource(R.drawable.indicator_inactive) // Inactive indicator
            }
        }
    }

    // Set the active state of the filter buttons
    private fun setActiveButton(activeButton: TextView) {
        buttonList.forEach { button ->
            if (button == activeButton) {
                button.setBackgroundResource(R.drawable.button_category_active)  // Active button background
                button.setTextColor(resources.getColor(R.color.white, null))      // Active button text color
            } else {
                button.setBackgroundResource(R.drawable.button_category_inactive)  // Inactive button background
                button.setTextColor(resources.getColor(R.color.black, null))      // Inactive button text color
            }
        }
    }
}
