package org.foi.air.smartcharger.fragments

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.databinding.FragmentChargerSimulatorBinding


class ChargerSimulatorFragment : Fragment() {

    private lateinit var binding: FragmentChargerSimulatorBinding
    lateinit var chronometer : Chronometer
    var pauseOffset : Long = 0
    lateinit var power : TextView
    lateinit var btnChangeState : ImageButton
    lateinit var btnDisconnect : Button
    var state = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChargerSimulatorBinding.inflate(inflater,container,false)

        chronometer = binding.chChargingTime
        power = binding.tvConsumedValue
        btnChangeState = binding.ibState
        btnDisconnect = binding.btnDisconnect

        btnChangeState.setOnClickListener{
            changeState()
        }



        return binding.root
    }

    private fun changeState() {
        state = if(state){

            btnChangeState.setImageResource(R.drawable.ic_start)
            pauseTimer()
            false

        }else{

            btnChangeState.setImageResource(R.drawable.ic_pause)
            startTimer()
            true

        }
    }

    private fun startTimer() {
        if(!state){
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
        }
    }

    private fun pauseTimer() {
        if(state){
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
        }
    }


}