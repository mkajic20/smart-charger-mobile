package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.foi.air.smartcharger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var that holds connection button state
        var state = true
        binding.btnConnectionButton.setOnClickListener()
        {
            //If button has "Connect" text on it
            if (state) {
                binding.btnConnectionButton.background = ContextCompat.getDrawable(this, R.drawable.custom_buttonbackground_cancel)
                binding.btnConnectionButton.text = resources.getString(R.string.cancel_button_text)
                binding.tvStatus.text = resources.getString(R.string.status_waiting_card)
                binding.tvInstructions.text = resources.getString(R.string.instructions_rfid_connection)
                state = false

            }
            //If button has "Cancel" text on it
            else {
                binding.btnConnectionButton.background = ContextCompat.getDrawable(this, R.drawable.custom_button)
                binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
                binding.tvInstructions.text = resources.getString(R.string.instructions_before_connection)
                binding.tvStatus.text = ""
                state = true

            }
        }

    }
}