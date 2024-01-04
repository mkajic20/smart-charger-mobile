package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.data_classes.ChargerInfo
import org.foi.air.core.models.ChargersResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.network.RequestHandler
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetChargersHandler(private var page : Int): RequestHandler<ChargersResponseBody>{
    override fun sendRequest(responseListener: ResponseListener<ChargersResponseBody>) {
        val service = ApiService.chargerService
        val serviceCall = service.getChargers(page)
        serviceCall.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val convertedResponse = successfulResponseConverter(response.body()!!)
                    if(convertedResponse == null){
                        Log.i("event", "Something went bad with your api call")
                    }else
                        responseListener.onSuccessfulResponse(convertedResponse)
                }else{
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), ErrorResponseBody::class.java)
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }

    private fun successfulResponseConverter(responseBody: ResponseBody): ChargersResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            val chargersArray = jsonResponse.getJSONArray("chargers")
            val chargerList = mutableListOf<ChargerInfo>()
            for (i in 0 until chargersArray.length()) {
                val chargerDataJson = chargersArray.getJSONObject(i)
                val eventElement = ChargerInfo(
                    chargerDataJson.getString("name"),
                    chargerDataJson.getDouble("latitude"),
                    chargerDataJson.getDouble("longitude"),
                    chargerDataJson.getString("creationTime"),
                    chargerDataJson.getBoolean("active"),
                    chargerDataJson.getInt("creatorId"),
                    chargerDataJson.getInt("id"),
                )
                chargerList.add(eventElement)
            }

            return ChargersResponseBody(success, message, chargerList)
        } catch (e: Exception) {
            Log.i("charger", e.message!!)
            return null
        }
    }

}