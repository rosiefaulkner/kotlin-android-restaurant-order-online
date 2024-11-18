package com.online_shop.kotlin_online_shop

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.online_shop.kotlin_online_shop.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private var aboutText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set page title
        setTitle(R.string.about_text_title)

        // Get the About Us text from the intent and set it to the TextView
        aboutText = binding.aboutText
        val intent = intent
        val title = intent.getStringExtra(getString(R.string.about_text_title))
        aboutText?.text = title

        // Make text scrollable
        aboutText?.movementMethod = ScrollingMovementMethod()
    }
}