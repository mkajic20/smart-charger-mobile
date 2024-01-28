package org.foi.air.smartcharger.context

import android.content.Context
import android.content.SharedPreferences

object Charger {
    private const val CARD_ID = "cardId"
    private const val CHARGER_ID = "chargerId"
    private const val CHARGER_NAME = "chargerName"
    private const val USER_ID = "userId"
    private const val EVENT_ID = "eventId"
    private const val START_TIME = "startTime"
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
            putString(EVENT_ID, eventId)
            putString(CARD_ID, cardId)
            putString(USER_ID, userId)
            putString(CHARGER_ID, chargerId)
            putString(CHARGER_NAME, chargerName)
            putLong(START_TIME, startTime!!)
            apply()
        }
        updateData()
    }
    private fun updateData() {
        eventId = storedChargerData?.getString(EVENT_ID, "")
        cardId = storedChargerData?.getString(CARD_ID, "")
        userId = storedChargerData?.getString(USER_ID, "")
        chargerId = storedChargerData?.getString(CHARGER_ID, "")
        chargerName = storedChargerData?.getString(CHARGER_NAME, "")
        startTime = storedChargerData?.getLong(START_TIME, 0)
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