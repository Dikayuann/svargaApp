package com.example.svargaapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.svargaapp.client.RetrofitClient
import com.example.svargaapp.response.history.HistoryResponse
import com.example.svargaapp.response.menu.MenuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var btnAll: TextView
    private lateinit var btnHistory: TextView
    private lateinit var btnProcess: TextView
    private lateinit var btnDraf: TextView

    private val buttonList = mutableListOf<TextView>()

    private val listHistory = ArrayList<HistoryResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView setup
        val RVHistory: RecyclerView = view.findViewById(R.id.rvHistory)
        RVHistory.layoutManager = LinearLayoutManager(context)

        // Mendapatkan data dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("user_pref", MODE_PRIVATE)
        // Mengambil userId dari SharedPreferences
        val userId = sharedPreferences.getInt("user_id", 0)
        // Fetch menu data from API
        RetrofitClient.instance.getHistory(userId).enqueue(
            object : Callback<ArrayList<HistoryResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<HistoryResponse>>,
                    response: Response<ArrayList<HistoryResponse>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            Log.d("HistoryFragment", "API response successful, items count: ${body.size}")
                            listHistory.clear()
                            listHistory.addAll(body)
                            val adapter = HistoryAdapter(listHistory)
                            RVHistory.adapter = adapter
                        } else {
                            Log.e("HistoryFragment", "Response body is null")
                        }
                    } else {
                        Log.e(
                            "HistoryFragment",
                            "API response not successful: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<HistoryResponse>>, t: Throwable) {
                    Log.e("HistoryFragment", "API call failed", t)
                }
            }
        )


        // Filter buttons setup
        btnAll = view.findViewById(R.id.btnAll)
        btnHistory = view.findViewById(R.id.btnHistory)
        btnProcess = view.findViewById(R.id.btnOnProcess)
        btnDraf = view.findViewById(R.id.btnDraft)

        buttonList.add(btnAll)
        buttonList.add(btnHistory)
        buttonList.add(btnProcess)
        buttonList.add(btnDraf)

        btnAll.setOnClickListener {
            setActiveButton(btnAll)
            val adapter = HistoryAdapter(listHistory) // Tampilkan semua data
            RVHistory.adapter = adapter
        }

        btnHistory.setOnClickListener {
            setActiveButton(btnHistory)
            val filteredList = listHistory.filter { it.status.equals("Selesai", ignoreCase = true) }
            val adapter = HistoryAdapter(ArrayList(filteredList))
            RVHistory.adapter = adapter
        }

        btnProcess.setOnClickListener {
            setActiveButton(btnProcess)
            val filteredList = listHistory.filter { it.status.equals("Dalam Proses", ignoreCase = true) }
            val adapter = HistoryAdapter(ArrayList(filteredList))
            RVHistory.adapter = adapter
        }

        btnDraf.setOnClickListener {
            setActiveButton(btnDraf)
            val filteredList = listHistory.filter { it.status.equals("Belum Bayar", ignoreCase = true) }
            val adapter = HistoryAdapter(ArrayList(filteredList))
            RVHistory.adapter = adapter
        }

    }

    private fun setActiveButton(activeButton: TextView) {
        // Reset semua button ke status tidak aktif
        buttonList.forEach { btn ->
            btn.setBackgroundResource(R.drawable.button_category_inactive)
            btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        // Set button yang aktif
        activeButton.setBackgroundResource(R.drawable.button_category_active)
        activeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.cream))
    }



    companion object {
    }
}