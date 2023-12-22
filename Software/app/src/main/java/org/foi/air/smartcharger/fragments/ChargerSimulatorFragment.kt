package org.foi.air.smartcharger.fragments

import ResponseListener
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import org.foi.air.api.models.StartEventBody
import org.foi.air.api.models.StopEventBody
import org.foi.air.api.request_handlers.StartChargingRequestHandler
import org.foi.air.api.request_handlers.StopChargingRequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.StartEventResponseBody
import org.foi.air.core.models.StopEventResponseBody
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Charger
import org.foi.air.smartcharger.databinding.FragmentChargerSimulatorBinding
import java.util.Calendar
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
    lateinit var eventId : String
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
        btnDisconnect.setOnClickListener{

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
            Log.i("punjenje", "ugasen")
            //pause charger
            btnChangeState.setImageResource(R.drawable.ic_start)
            changeStateInstruction.text = getString(R.string.start_charger_instruction)
            pauseTimer()
            stopCharging()
            false

        }else{
            Log.i("punjenje", "upaljen")
            //start charger
            btnChangeState.setImageResource(R.drawable.ic_pause)
            changeStateInstruction.text = getString(R.string.pause_charger_instruction)
            startTimer()
            Log.i("punjenje", getTime())
            startCharging()
            true

        }
    }

    private fun stopCharging() {
        val eventBody = StopEventBody(
            getTime(),
            power.text.toString(),
            eventId,
        )
        Log.i("punjenje", getTime())
        Log.i("punjenje", power.text.toString())
        Log.i("punjenje", eventBody.Id)
        val startChargingHandler = StopChargingRequestHandler(eventBody)
        startChargingHandler.sendRequest(object: ResponseListener<StopEventResponseBody>{
            override fun onSuccessfulResponse(response: StopEventResponseBody) {
                Log.i("punjenje", response.message)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("punjenje", response.message)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("punjenje", t.message!!)
            }

        })
    }

    private fun startCharging() {
        val eventBody = StartEventBody(
            getTime(),
            Charger.cardId,
            Charger.chargerId,
            Charger.userId
        )
        val startChargingHandler = StartChargingRequestHandler(eventBody)
        startChargingHandler.sendRequest(object: ResponseListener<StartEventResponseBody>{
            override fun onSuccessfulResponse(response: StartEventResponseBody) {
                Log.i("punjenje", response.message)
                if(response.event.eventId != null){
                    Log.i("punjenje", response.event.eventId)
                    eventId = response.event.eventId
                }

            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("punjenje", response.message)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("punjenje", t.message!!)
            }

        })
    }

    private fun getTime(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val formattedMinute = String.format("%02d", minute)
        val formattedSecond = String.format("%02d", second)
        return "$year-$month-${day}T$hour:$formattedMinute:${formattedSecond}Z"
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