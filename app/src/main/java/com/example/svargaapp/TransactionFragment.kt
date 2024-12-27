package com.example.svargaapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var paymentName: TextView
    private lateinit var paymentDescription: TextView
    private lateinit var paymentLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

        // Inisialisasi view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi view
        paymentName = view.findViewById(R.id.textPaymentQris)
        paymentDescription = view.findViewById(R.id.textPaymentDesc)
        paymentLogo = view.findViewById(R.id.paymentLogo)

        // Tombol untuk membuka bottom sheet
        val btnCheckPayment: TextView = view.findViewById(R.id.textPaymentOtherArrow)
        btnCheckPayment.setOnClickListener {
            openPaymentBottomSheet()
        }

        val tvCheckPayment: TextView = view.findViewById(R.id.textPaymentOther)
        tvCheckPayment.setOnClickListener {
            openPaymentBottomSheet()
        }
    }

    // TransactionFragment
    private fun openPaymentBottomSheet() {
        Log.d("TransactionFragment", "Opening PaymentMethodBottomSheet")
        val bottomSheet = PaymentMethodBottomSheet { selectedPayment ->
            Log.d("TransactionFragment", "Selected payment received: ${selectedPayment.name}")
            updatePaymentMethod(selectedPayment)
        }
        bottomSheet.show(parentFragmentManager, "PaymentMethodBottomSheet")
    }


    private fun updatePaymentMethod(paymentMethod: PaymentMethodModel) {
        // Perbarui UI berdasarkan pilihan pengguna
        paymentName.text = paymentMethod.name
        paymentDescription.text = paymentMethod.description
        paymentLogo.setImageResource(paymentMethod.image)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}