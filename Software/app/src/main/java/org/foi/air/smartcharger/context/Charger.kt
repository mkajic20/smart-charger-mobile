package org.foi.air.smartcharger.context

import android.content.Context
import android.content.SharedPreferences

object Charger {
    //dummy data for chargerId
    var cardId : String? = ""
    var chargerId: String? = ""
    var chargerName: String? = ""
    var userId: String? = ""
    var eventId : String? = ""
    var startTime: Long? = 0
    private var storedChargerData : SharedPreferences? = null
    fun initialize(context: Context){
        storedChargerData = context.getSharedPreferences("charger", Context.MODE_PRIVATE)
        updateData()
    }

    fun saveChargerData(){
        val editor = storedChargerData?.edit()
        editor?.apply{
            putString("eventId", eventId)
            putString("cardId", cardId)
            putString("userId", userId)
            putString("chargerId", chargerId)
            putString("chargerName", chargerName)
            putLong("chronometerBase", startTime!!)
            apply()
        }
        updateData()
    }
    private fun updateData() {
        eventId = storedChargerData?.getString("eventId", "")
        cardId = storedChargerData?.getString("cardId", "")
        userId = storedChargerData?.getString("userId", "")
        chargerId = storedChargerData?.getString("chargerId", "")
        chargerName = storedChargerData?.getString("chargerName", "")
        startTime = storedChargerData?.getLong("chronometerBase", 0)
    }

    fun deleteChargerData(){
        val editor = storedChargerData?.edit()
        editor?.clear()
        editor?.apply()
        updateData()
    }

    fun isChargerConnected() : Boolean{
        return cardId != "" && userId != "" && chargerId != ""
    }

}