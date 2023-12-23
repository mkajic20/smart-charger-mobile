package org.foi.air.smartcharger.context

import android.content.Context
import android.content.SharedPreferences

object Charger {
    //dummy data for chargerId
    var cardId : String? = ""
    var chargerId: String = "5"
    var userId: String? = ""
    var eventId : String? = ""
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
            apply()
        }
        updateData()
    }
    private fun updateData() {
        eventId = storedChargerData?.getString("eventId", "")
        cardId = storedChargerData?.getString("cardId", "")
        userId = storedChargerData?.getString("userId", "")
    }

    fun deleteChargerData(){
        val editor = storedChargerData?.edit()
        editor?.clear()
        editor?.apply()
        updateData()
    }

    fun isChargerConnected() : Boolean{
        return cardId != "" && userId != ""
    }

}