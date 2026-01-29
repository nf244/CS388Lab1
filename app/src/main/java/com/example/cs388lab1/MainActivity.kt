package com.example.cs388lab1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var countTextView: TextView
    private lateinit var tapButton: Button
    private lateinit var upgradeButton: Button

    private var count = 0
    private var tapMultiplier = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load saved data
        val prefs = getSharedPreferences("TapCounterPrefs", Context.MODE_PRIVATE)
        count = prefs.getInt("count", 0)
        tapMultiplier = prefs.getInt("multiplier", 1)

        // Initialize views
        countTextView = findViewById(R.id.countTextView)
        tapButton = findViewById(R.id.tapButton)
        upgradeButton = findViewById(R.id.upgradeButton)

        // Set initial text
        updateCountDisplay()
        updateUpgradeButton()

        // Tap button behavior
        tapButton.setOnClickListener {
            count += tapMultiplier
            updateCountDisplay()
            updateUpgradeButton()
        }

        // Upgrade button behavior
        upgradeButton.setOnClickListener {
            if (count >= 100) {
                count -= 100
                tapMultiplier++
                updateCountDisplay()
                updateUpgradeButton()

                Toast.makeText(this, "🎉 Upgrade purchased! Multiplier: ${tapMultiplier}x 🎉",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Auto-save when app goes to background
        getSharedPreferences("TapCounterPrefs", Context.MODE_PRIVATE).edit().apply {
            putInt("count", count)
            putInt("multiplier", tapMultiplier)
            apply()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateCountDisplay() {
        countTextView.text = "Count: $count"
    }

    @SuppressLint("SetTextI18n")
    private fun updateUpgradeButton() {
        if (count >= 100) {
            upgradeButton.isEnabled = true
            upgradeButton.text = "Upgrade (100 taps) - ${tapMultiplier + 1}x"
        } else {
            upgradeButton.isEnabled = false
            upgradeButton.text = "Need ${100 - count} more taps"
        }
    }
}