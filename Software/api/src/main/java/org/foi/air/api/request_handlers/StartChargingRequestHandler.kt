package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.models.StartEventBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.data_classes.EventInfo
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.StartEventResponseBody
import org.foi.air.core.network.RequestHandler
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartChargingRequestHandler(private val requestBody: StartEventBody): RequestHandler<StartEventResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<StartEventResponseBody>) {
        val service = ApiService.eventService
        val serviceCall = service.startEvent(requestBody)

        serviceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val convertedResponse = successfulResponseConverter(response.body()!!)
                    responseListener.onSuccessfulResponse(convertedResponse!!)
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

    private fun successfulResponseConverter(responseBody: ResponseBody): StartEventResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            val eventDataJson = jsonResponse.getJSONObject("event")
            val eventInfo = EventInfo(
                eventDataJson.getString("id"),
                eventDataJson.getString("chargerId"),
                eventDataJson.getString("startTime"),
                eventDataJson.getString("endTime"),
                eventDataJson.getString("volume")

            )

            return StartEventResponseBody(success, message, eventInfo)
        } catch (e: Exception) {
            return null
        }
    }

}