package com.online_shop.kotlin_online_shop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.online_shop.kotlin_online_shop.databinding.ActivityOrderHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date

/** @noinspection resource
 */
class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize order page UI elements / widgets
        initWidgets()

        // initialize variables
        val orderModalArrayList: ArrayList<OrderModel>
        val dbHandler = DBHandler(this)

        // Get orders list from db handler
        orderModalArrayList = dbHandler.readOrders()

        // If orders list is empty, set text to "No orders found"
        if (orderModalArrayList.isEmpty()) {
            binding.orderHistoryText.text = getString(R.string.no_orders_found)
        }

        val result = extractStrings(orderModalArrayList)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result)
        binding.orderHistoryListBody.adapter = adapter
    }


    /**
     * Initialize widgets
     */
    private fun initWidgets() {
        // Set page title
        title = getString(R.string.order_history)

        // Get the order history data from the intent
        val intent: Intent = intent
        val body = intent.getStringExtra(getString(R.string.order_history))
        binding.orderHistoryText.text = body
    }

    /**
     * Extract the strings from the order model in a list of formatted string values
     *
     * @param orderModelArrayList The order model array list
     *
     * @return The list of strings
     */
    private fun extractStrings(orderModelArrayList: ArrayList<OrderModel>): List<String> {
        val extractedStrings: MutableList<String> = ArrayList()

        for (order in orderModelArrayList) {
            val orderTitle: String = getString(R.string.order_history_item_title) +
                    order.id.toString() +
                    getString(R.string.order_history_item_title_ending)

            val orderDetails = ((((("""
    
    $orderTitle
    ${getString(R.string.size_label)}
    """.trimIndent()) +
                    order.size +
                    "\n" +
                    getString(R.string.flavor_label)) +
                    order.flavor +
                    "\n" +
                    getString(R.string.fudge_label)) +
                    order.fudge +
                    "\n" +
                    getString(R.string.date_label)) +
                    convertToReadableDate(order.createdAt) +
                    "\n" +
                    getString(R.string.price_label)) +
                    order.price +
                    "\n"

            extractedStrings.add(orderDetails)
        }

        return extractedStrings
    }


    companion object {
        /**
         * Convert the epoch time to a readable date
         *
         * @param timestampStr The epoch time
         *
         * @return The readable date
         */
        fun convertToReadableDate(timestampStr: String): String {
            try {
                val timestampMillis = timestampStr.toLong()
                val date = Date(timestampMillis)
                @SuppressLint("SimpleDateFormat") val formatter =
                    SimpleDateFormat("MMM d, yyyy @ h:mm a")
                return formatter.format(date)
            } catch (e: NumberFormatException) {
                // The timestamp string is not a valid long
                return "Invalid Date"
            }
        }
    }
}