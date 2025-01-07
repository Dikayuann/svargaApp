package com.example.svargaapp

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class TransactionFragment : Fragment() {

    private lateinit var paymentName: TextView
    private lateinit var paymentDescription: TextView
    private lateinit var paymentLogo: ImageView

    // Views for Delivery and Take Away
    private lateinit var deliveryButton: ConstraintLayout
    private lateinit var takeAwayButton: ConstraintLayout

    // Track the selected option
    private var isDeliverySelected = false
    private var isTakeAwaySelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        paymentName = view.findViewById(R.id.textPaymentQris)
        paymentDescription = view.findViewById(R.id.textPaymentDesc)
        paymentLogo = view.findViewById(R.id.paymentLogo)

        // Initialize Delivery and Take Away buttons
        deliveryButton = view.findViewById(R.id.widgetDelivery)
        takeAwayButton = view.findViewById(R.id.widgetTakeAway)

        // Set up click listeners for Delivery and Take Away
        deliveryButton.setOnClickListener {
            toggleDelivery()
        }

        takeAwayButton.setOnClickListener {
            toggleTakeAway()
        }

        // Other code for handling payment method
        val btnCheckPayment: TextView = view.findViewById(R.id.textPaymentOtherArrow)
        btnCheckPayment.setOnClickListener {
            openPaymentBottomSheet()
        }

        val tvCheckPayment: TextView = view.findViewById(R.id.textPaymentOther)
        tvCheckPayment.setOnClickListener {
            openPaymentBottomSheet()
        }
    }

    // Open Payment Method Bottom Sheet
    private fun openPaymentBottomSheet() {
        Log.d("TransactionFragment", "Opening PaymentMethodBottomSheet")
        val bottomSheet = PaymentMethodBottomSheet { selectedPayment ->
            Log.d("TransactionFragment", "Selected payment received: ${selectedPayment.name}")
            updatePaymentMethod(selectedPayment)
        }
        bottomSheet.show(parentFragmentManager, "PaymentMethodBottomSheet")
    }

    // Update the payment method UI
    private fun updatePaymentMethod(paymentMethod: PaymentMethodModel) {
        paymentName.text = paymentMethod.name
        paymentDescription.text = paymentMethod.description
        paymentLogo.setImageResource(paymentMethod.image)
    }

    // Handle Delivery selection
    private fun toggleDelivery() {
        isDeliverySelected = true
        isTakeAwaySelected = false

        // Change colors based on selection
        updateButtonColors()

        // Perform other actions related to Delivery selection
    }

    // Handle Take Away selection
    private fun toggleTakeAway() {
        isTakeAwaySelected = true
        isDeliverySelected = false

        // Change colors based on selection
        updateButtonColors()

        // Perform other actions related to Take Away selection
    }

    // Update the background color of Delivery and Take Away buttons
    private fun updateButtonColors() {
        // Check if the Delivery option is selected
        if (isDeliverySelected) {
            // Change the background color of the delivery button to blue
            setButtonBackgroundColor(deliveryButton, R.color.blue)
            setButtonBackgroundColor(takeAwayButton, R.color.defaultColor)
        } else if (isTakeAwaySelected) {
            // Change the background color of the take away button to blue
            setButtonBackgroundColor(takeAwayButton, R.color.blue)
            setButtonBackgroundColor(deliveryButton, R.color.defaultColor)
        }
    }

//    companion object {
//        // Factory method to create a new instance of the fragment
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            TransactionFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    // Helper function to set background color programmatically
    private fun setButtonBackgroundColor(button: ConstraintLayout, colorResId: Int) {
        val drawable = button.background as GradientDrawable
        drawable.setColor(resources.getColor(colorResId))
    }

}
