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
import org.foi.air.smartcharger.MainActivity
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Charger
import org.foi.air.smartcharger.databinding.FragmentChargerSimulatorBinding
import java.util.Calendar
import java.util.Locale


class ChargerSimulatorFragment : Fragment() {

    private lateinit var binding: FragmentChargerSimulatorBinding
    lateinit var chronometer : Chronometer
    var lastUpdateTime = 0L
    lateinit var tvState: TextView
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
        val startTime = Charger.startTime

        tvState = binding.tvChargingStatus
        chronometer = binding.chChargingTime
        power = binding.tvConsumedValue
        btnChangeState = binding.ibState
        changeStateInstruction = binding.tvChangeStateInstructions
        btnDisconnect = binding.btnDisconnect
        //in case app closed while charging was on
        if(Charger.eventId!="") {
            val elapsedMillis = System.currentTimeMillis() - startTime!!
            chronometer.base = SystemClock.elapsedRealtime() - elapsedMillis
            changeInterfaceStart()
            chronometer.start()
            state = true
        }
        btnChangeState.setOnClickListener{
            changeState()
        }

        chronometer.setOnChronometerTickListener { chronometer ->
            val elapsedMillis = SystemClock.elapsedRealtime()

            // Check if 10 seconds have passed since the last update
            if (elapsedMillis - lastUpdateTime >= 1000) {
                updatePower()
                lastUpdateTime = elapsedMillis
            }
        }

        btnDisconnect.setOnClickListener{

            if(state)
                stopCharging()
            Charger.deleteChargerData()
            (requireActivity() as MainActivity).changeFragment("ChargerConnectionFragment")
            Log.i("punjenje", "Disconnected from charger")
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
            //pause charger
            stopCharging()
            false

        }else{
            //start charger
            startCharging()
            true

        }
    }

    private fun stopCharging() {
        //in case there is no running event
        if(Charger.eventId == "")
            return
        val eventBody = StopEventBody(
            getTime(),
            power.text.toString(),
            Charger.eventId!!,
        )

        val stopChargingHandler = StopChargingRequestHandler(eventBody)
        stopChargingHandler.sendRequest(object: ResponseListener<StopEventResponseBody>{
            override fun onSuccessfulResponse(response: StopEventResponseBody) {
                Log.i("punjenje", response.message)
                changeInterfaceStop()
                pauseTimer()

            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("punjenje", response.message)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("punjenje", t.message!!)
            }

        })
        Charger.eventId=""
        Charger.startTime = 0
        Charger.saveChargerData()
    }

    private fun startCharging() {
        val eventBody = StartEventBody(
            getTime(),
            Charger.cardId!!,
            Charger.chargerId,
            Charger.userId!!
        )
        val startChargingHandler = StartChargingRequestHandler(eventBody)
        startChargingHandler.sendRequest(object: ResponseListener<StartEventResponseBody>{
            override fun onSuccessfulResponse(response: StartEventResponseBody) {
                val startTime = System.currentTimeMillis()
                Charger.startTime = startTime
                Charger.eventId = response.event.eventId
                Charger.saveChargerData()
                Log.i("punjenje", response.message)
                changeInterfaceStart()
                if(Charger.eventId != "")
                    startTimer()

            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("punjenje", response.message)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("punjenje", t.message!!)
            }

        })
    }

    private fun changeInterfaceStart(){
        btnChangeState.setImageResource(R.drawable.ic_pause)
        changeStateInstruction.text = getString(R.string.pause_charger_instruction)
        tvState.text = getString(R.string.status_charging)
    }
    private fun changeInterfaceStop(){
        btnChangeState.setImageResource(R.drawable.ic_start)
        changeStateInstruction.text = getString(R.string.start_charger_instruction)
        tvState.text = getString(R.string.status_not_charging)
        power.text = "0.0"
    }

    private fun getTime(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val formattedMonth = String.format("%02d", month)
        val formattedDay = String.format("%02d", day)
        val formattedHour = String.format("%02d", hour)
        val formattedMinute = String.format("%02d", minute)
        val formattedSecond = String.format("%02d", second)
        return "$year-$formattedMonth-${formattedDay}T$formattedHour:$formattedMinute:${formattedSecond}Z"
    }

    private fun startTimer() {
        if(state){
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        }
    }

    private fun pauseTimer() {
        if(!state){
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
        }
    }


}