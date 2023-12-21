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
import java.util.Locale


class ChargerSimulatorFragment : Fragment() {

    private lateinit var binding: FragmentChargerSimulatorBinding
    lateinit var chronometer : Chronometer
    var pauseOffset : Long = 0
    var lastUpdateTime = 0L
    lateinit var power : TextView
    lateinit var btnChangeState : ImageButton
    lateinit var changeStateInstruction : TextView
    lateinit var btnDisconnect : Button
    var state = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargerSimulatorBinding.inflate(inflater,container,false)

        chronometer = binding.chChargingTime
        power = binding.tvConsumedValue
        btnChangeState = binding.ibState
        changeStateInstruction = binding.tvChangeStateInstructions
        btnDisconnect = binding.btnDisconnect

        btnChangeState.setOnClickListener{
            changeState()

            chronometer.setOnChronometerTickListener { chronometer ->
                val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base

                // Check if 10 seconds have passed since the last update
                if (elapsedMillis - lastUpdateTime >= 1000) {
                    // Update the power variable or perform any other actions
                    updatePower()
                    lastUpdateTime = elapsedMillis
                }
            }

        }



        return binding.root
    }

    private fun updatePower() {

        //If charger has 7.5kW then 7.5/3600 = 0.00208 - every second car gets 0.0028 kW
        val newPower = 0.00208 * ((SystemClock.elapsedRealtime() - chronometer.base) / 1000)
        power.text = String.format(Locale.US, "%.4f", newPower)



    }

    private fun changeState() {
        state = if(state){

            btnChangeState.setImageResource(R.drawable.ic_start)
            changeStateInstruction.text = getString(R.string.start_charger_instruction)
            pauseTimer()
            false

        }else{

            btnChangeState.setImageResource(R.drawable.ic_pause)
            changeStateInstruction.text = getString(R.string.pause_charger_instruction)
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