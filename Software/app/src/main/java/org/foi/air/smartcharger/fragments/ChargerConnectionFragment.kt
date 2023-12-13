package org.foi.air.smartcharger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.databinding.FragmentChargerConnectionBinding


class ChargerConnectionFragment : Fragment() {

    private lateinit var binding: FragmentChargerConnectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.initialize(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargerConnectionBinding.inflate(inflater,container,false)
        var state = true
        binding.btnConnectionButton.setOnClickListener()
        {
            //If button has "Connect" text on it
            if (state) {
                binding.btnConnectionButton.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.custom_buttonbackground_cancel)
                binding.btnConnectionButton.text = resources.getString(R.string.cancel_button_text)
                binding.tvStatus.text = resources.getString(R.string.status_waiting_card)
                binding.tvInstructions.text = resources.getString(R.string.instructions_rfid_connection)
                state = false

            }
            //If button has "Cancel" text on it
            else {
                binding.btnConnectionButton.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.custom_blue_button)
                binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
                binding.tvInstructions.text = resources.getString(R.string.instructions_before_connection)
                binding.tvStatus.text = ""
                state = true

            }
        }




        return binding.root
    }





}