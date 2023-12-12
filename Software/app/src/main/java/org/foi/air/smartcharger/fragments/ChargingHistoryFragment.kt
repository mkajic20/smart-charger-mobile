package org.foi.air.smartcharger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.databinding.FragmentChargingHistoryBinding


class ChargingHistoryFragment : Fragment() {

    private lateinit var binding: FragmentChargingHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.initialize(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargingHistoryBinding.inflate(inflater,container,false)



        return binding.root
    }

}