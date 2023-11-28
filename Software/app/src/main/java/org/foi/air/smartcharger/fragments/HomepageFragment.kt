package org.foi.air.smartcharger.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.databinding.ActivityMainBinding
import org.foi.air.smartcharger.databinding.FragmentHomepageBinding

class HomepageFragment : Fragment() {

    private lateinit var binding : FragmentHomepageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepageBinding.inflate(layoutInflater)

        Log.i("dfoawnjkafnskjn","I am in fragment!")

        var state = true
        binding.btnConnectionButton.setOnClickListener()
        {
            //If button has "Connect" text on it
            if (state) {
                binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_buttonbackground_cancel)
                binding.btnConnectionButton.text = resources.getString(R.string.cancel_button_text)
                binding.tvStatus.text = resources.getString(R.string.status_waiting_card)
                binding.tvInstructions.text = resources.getString(R.string.instructions_rfid_connection)
                state = false

            }
            //If button has "Cancel" text on it
            else {
                binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_button)
                binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
                binding.tvInstructions.text = resources.getString(R.string.instructions_before_connection)
                binding.tvStatus.text = ""
                state = true

            }
        }

        return binding.root
    }
}