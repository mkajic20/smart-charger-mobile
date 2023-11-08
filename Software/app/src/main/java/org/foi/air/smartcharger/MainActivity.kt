package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button1)
        val status = findViewById<TextView>(R.id.status)
        val instructions = findViewById<TextView>(R.id.instructions)
        var state = true
        button.setOnClickListener()
        {
            if (state) {
                button.setBackground(resources.getDrawable(R.drawable.custom_buttonbackground_cancel))
                button.text = "Cancel"
                status.text = "Status: Waiting for card..."
                instructions.text =
                    "Please put your RFID card on the phone screen to connect with the charger"
                state = false

            } else {
                button.setBackground(resources.getDrawable(R.drawable.custom_button_background))
                button.text = "Connect"
                instructions.text =
                    "Before you start using charger you will have to connect with your RFID card."
                status.text = ""
                state = true

            }
        }

    }
}