package com.online_shop.kotlin_online_shop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.online_shop.kotlin_online_shop.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private var fudgeAmount: SeekBar? = null
    @JvmField
    var total: Float? = null
    private lateinit var toppings: Array<CheckBox>
    private var dbHandler: DBHandler? = null
    private val addAction = "add"
    private val subtractAction = "subtract"

    val previousProgress: IntArray = intArrayOf(1)
    val previousSize: IntArray = intArrayOf(0)

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize views
        initWidgets()

        // Initialize database handler
        dbHandler = DBHandler(this@MainActivity)
    }

    /**
     * Enum for storing size prices
     */
    enum class Size(val price: Float) {
        SMALL(2.99f),
        MEDIUM(3.99f),
        LARGE(4.99f)
    }

    /**
     * Enum for storing hot fudge amount prices
     * Throws an exception if no matching enum constant is found.
     */
    enum class HotFudge(@JvmField val ounces: Int, val price: Float) {
        ZERO_OUNCES(0, 0.00f),
        ONE_OUNCE(1, 0.15f),
        TWO_OUNCES(2, 0.25f),
        THREE_OUNCES(3, 0.30f);

        companion object {
            // Get HotFudge enum based on selected ounce(s)
            fun fromOunces(ounces: Int): HotFudge {
                for (fudgeAmount in entries) {
                    if (fudgeAmount.ounces == ounces) {
                        return fudgeAmount
                    }
                }
                // If no match is found, throw an exception
                throw IllegalArgumentException("No HotFudge enum constant with $ounces ounces")
            }
        }
    }

    /**
     * Enum for storing topping prices.
     */
    enum class Topping(val price: Float) {
        PEANUTS(0.15f),
        M_AND_MS(0.25f),
        ALMONDS(0.15f),
        BROWNIE(0.20f),
        STRAWBERRIES(0.20f),
        OREOS(0.20f),
        GUMMY_BEARS(0.20f),
        MARSHMALLOWS(0.15f)
    }

    /**
     * Initializes widgets
     */
    private fun initWidgets() {
        // Initialize Fudge slider and set listener
        initFudgeSlider()

        // Initialize flavor/size dropdowns
        initFlavorDropdown()
        initSizeDropdown()

        // Initialize buttons
        initButtons()

        // Initialize toppings checkboxes and set listeners
        initToppings()

        // Initialize price
        initPrice()
    }

    /**
     * Initializes hot fudge slider and sets listener
     */
    private fun initFudgeSlider() {
        binding.amountFudge.progress = HotFudge.ONE_OUNCE.ounces
        binding.amountFudge.setOnSeekBarChangeListener(onFudgeAmountChanged())

        binding.fudgeProgressText.text = String.format("%s", getString(R.string.progress_1oz))
    }

    /**
     * Initializes flavor dropdown
     */
    private fun initFlavorDropdown() {
        onFlavorDropdownSelect()
    }

    /**
     * Initializes size dropdown
     */
    private fun initSizeDropdown() {
        // Size dropdown listener
        onSizeDropdownSelect()
    }

    /**
     * Initializes buttons and sets respective listeners
     */
    private fun initButtons() {
        // Order button
        binding.order.setOnClickListener { this.onOrderClick() }
        // The Works! button
        binding.theWorks.setOnClickListener { this.onTheWorksClick() }
        // Reset button
        binding.reset.setOnClickListener { this.onResetClick() }
    }

    /**
     * Initializes toppings and set listener
     */
    @Suppress("NAME_SHADOWING")
    private fun initToppings() {
        toppings = arrayOf(
            binding.peanuts,
            binding.mAndMs,
            binding.almonds,
            binding.brownie,
            binding.strawberries,
            binding.oreos,
            binding.gummyBears,
            binding. marshmallows
        )

        for (topping in toppings) {
            topping.setOnCheckedChangeListener { topping: CompoundButton, checked: Boolean ->
                this.onCheckboxChecked(
                    topping,
                    checked
                )
            }
        }
    }

    /**
     * Initializes total price with default 1 oz hot fudge set
     */
    @SuppressLint("DefaultLocale")
    private fun initPrice() {
        total = HotFudge.ONE_OUNCE.price
        binding.priceLabelText.text = getString(R.string.price_format, total)
    }

    /**
     * Resets the form to its default state.
     */
    private fun onResetClick() {
        // Reset fudge amount to 1 oz & update corresponding text
        binding.fudgeProgressText.text = String.format("%s", getString(R.string.progress_1oz))
        binding.amountFudge.progress = HotFudge.ONE_OUNCE.ounces
        // Reset size to small
        binding.size.setSelection(Size.SMALL.ordinal)
        // Reset flavor to vanilla
        binding.flavor.setSelection(0)
        for (topping in toppings) {
            this.toggleCheckbox(false, topping)
        }
    }

    /**
     * Toggles the checked state of a checkbox.
     *
     * @param checked The new checked state of the checkbox.
     * @param c The checkbox to be toggled.
     */
    private fun toggleCheckbox(checked: Boolean, c: CheckBox) {
        c.isChecked = checked
    }

    /**
     * Handles the checked state of a checkbox.
     *
     * @param topping The checkbox that was checked or unchecked.
     * @param checked The new checked state of the checkbox.
     *
     * Throws exception if no matching enum constant is found.
     */
    private fun onCheckboxChecked(topping: CompoundButton, checked: Boolean) {
        val toppingID = topping.id
        val toppingResourceName =
            resources.getResourceEntryName(toppingID).uppercase(Locale.getDefault())

        try {
            val selectedTopping = Topping.valueOf(toppingResourceName)
            val price = selectedTopping.price
            // If the checkbox is checked, add the topping price to the total, else subtract it
            if (checked) {
                calculatePrice(price, addAction)
            } else {
                calculatePrice(price, subtractAction)
            }
        } catch (e: IllegalArgumentException) {
            // Throw exception if no matching topping is found
            throw IllegalArgumentException("No topping enum constant for $toppingResourceName")
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param priceChange The change in price.
     * @param action The action to be performed.
     */
    @SuppressLint("DefaultLocale")
    private fun calculatePrice(priceChange: Float, action: String) {
        if (action == addAction) {
            total = total!! + priceChange
        } else if (action == subtractAction) {
            total = total!! - priceChange
        }
        binding.priceLabelText.text = getString(R.string.price_format, total)
    }

    /**
     * The user can click the button labeled,
     * 'The Works!' and they will get a large
     * sundae with everything on it
     */
    private fun onTheWorksClick() {
        binding.amountFudge.progress = HotFudge.THREE_OUNCES.ounces
        binding.size.setSelection(Size.LARGE.ordinal)
        binding.flavor.setSelection(0)
        for (topping in toppings) {
            this.toggleCheckbox(true, topping)
        }
    }

    /**
     * Handles changes in the fudge amount.
     *
     * @return [SeekBar.OnSeekBarChangeListener]
     */
    private fun onFudgeAmountChanged(): OnSeekBarChangeListener {
        return object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.fudgeProgressText.text =
                    String.format("%s %s", progress, getString(R.string.oz))
                val selectedFudge = HotFudge.fromOunces(progress)
                val fudgePrice = selectedFudge.price
                calculatePrice(fudgePrice, addAction)

                // Subtract previous selected progress price
                if (previousProgress[0] != progress) {
                    val previousFudge = HotFudge.fromOunces(previousProgress[0])
                    val previousFudgePrice = previousFudge.price
                    calculatePrice(previousFudgePrice, subtractAction)
                }

                // Set previousProgress to current
                previousProgress[0] = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    /**
     * Size dropdown handler. Increments/decrements total price based on selected size.
     */
    private fun onSizeDropdownSelect() {
        binding.size.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedSizeText = parent.getItemAtPosition(position).toString()
                    .uppercase(Locale.getDefault())
                val selectedSize =
                    Size.valueOf(selectedSizeText)
                val sizePrice = selectedSize.price
                calculatePrice(sizePrice, addAction)

                // Subtract previous size price
                val prevSizePosition = previousSize[0]
                if (prevSizePosition != position) {
                    val previousSizeText =
                        parent.getItemAtPosition(prevSizePosition).toString()
                            .uppercase(Locale.getDefault())
                    val previousSizeEnum =
                        Size.valueOf(previousSizeText)
                    val previousSizePrice = previousSizeEnum.price
                    calculatePrice(previousSizePrice, subtractAction)
                }

                // Set prevSize to current position
                previousSize[0] = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Not needed
            }
        }
    }

    /**
     * Flavor dropdown handler
     */
    private fun onFlavorDropdownSelect() {
        binding.flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                // Not needed
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Not needed
            }
        }
    }

    /**
     * Creates the options menu for the activity.
     *
     * @param menu The menu to be created.
     *
     * @return `true` if the menu was created, `false` otherwise.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Handles item selections in the navigation (options) menu. This method is called when the user
     * selects an item from the options menu. It identifies the selected item by its ID and performs
     * the corresponding action, such as starting an activity, i.e. navigating to a page in the
     * user's perspective.
     *
     * @param item The selected menu item.
     *
     * @return `true` if the item selection was handled, `false` otherwise.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                val intent = Intent(
                    this@MainActivity,
                    AboutActivity::class.java
                )
                intent.putExtra(
                    getString(R.string.about_text_title),
                    getString(R.string.about_text_body)
                )
                startActivity(intent)
                return true
        }
            R.id.order_history -> {
                val intent = Intent(
                    this@MainActivity,
                    OrderHistoryActivity::class.java
                )
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Handles the click event of the "Order" button.
     */
    private fun onOrderClick() {
        // Get the selected values from the order form
        val orderSize = binding.size.selectedItem.toString()
        val orderFlavor = binding.flavor.selectedItem.toString()
        val orderFudge = binding.fudgeProgressText.text.toString()
        val orderPrice = getString(R.string.price_format, total)

        // Validate size and flavor inputs
        if (!validateSpinners(orderSize, orderFlavor)) {
            return
        }

        // Add order to database
        dbHandler!!.addNewOrder(orderSize, orderFlavor, orderFudge, orderPrice)

        // Reset the form
        Toast.makeText(
            this@MainActivity,
            getString(R.string.order_successfully_placed),
            Toast.LENGTH_SHORT
        ).show()
        this.onResetClick()
    }

    /**
     * Validates the size and flavor inputs and the price calculation. Provides a warning to the user
     * and prevents the order from being placed if the inputs are invalid. This provides a rudimentary
     * safeguard against SQL injection and adds cursory fault tolerance.
     *
     * @param orderSize The selected size.
     * @param orderFlavor The selected flavor.
     *
     * @return `true` if the inputs are valid, `false` otherwise.
     */
    private fun validateSpinners(orderSize: String, orderFlavor: String): Boolean {
        var isValidSize = false
        if (orderSize.isEmpty()) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.please_enter_a_size),
                Toast.LENGTH_SHORT
            ).show()
        }
        val sizes = resources.getStringArray(R.array.size_names)
        for (size in sizes) {
            if (size.equals(orderSize, ignoreCase = true)) {
                isValidSize = true
                break
            }
        }
        val flavors = resources.getStringArray(R.array.flavor_names)
        var isValidFlavor = false
        if (orderFlavor.isEmpty()) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.please_enter_a_flavor),
                Toast.LENGTH_SHORT
            ).show()
        }
        for (flavor in flavors) {
            if (flavor.equals(orderFlavor, ignoreCase = true)) {
                isValidFlavor = true
                break
            }
        }
        var isValidPrice = false
        if (total!! >= Size.SMALL.price) {
            isValidPrice = true
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.please_make_selection),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValidSize && isValidFlavor && isValidPrice
    }
}