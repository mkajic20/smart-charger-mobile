package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.data_classes.EventInfo
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.EventsResponseBody
import org.foi.air.core.network.RequestHandler
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetEventsForUserRequestHandler(private var userId : Int, private var page : Int): RequestHandler<EventsResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<EventsResponseBody>) {
        val service = ApiService.eventService
        val serviceCall = service.getEvents(userId, page)
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

    private fun successfulResponseConverter(responseBody: ResponseBody): EventsResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            val eventArray = jsonResponse.getJSONArray("events")
            val eventList = mutableListOf<EventInfo>()
            for (i in 0 until eventArray.length()) {
                val eventDataJson = eventArray.getJSONObject(i)
                val charger = eventDataJson.getJSONObject("charger")
                val eventElement = EventInfo(
                    eventDataJson.getString("id"),
                    charger.getString("id"),
                    eventDataJson.getString("startTime"),
                    eventDataJson.getString("endTime"),
                    eventDataJson.getString("volume")
                )
                eventList.add(eventElement)
            }

            return EventsResponseBody(success, message, eventList)
        } catch (e: Exception) {
            Log.i("event", e.message!!)
            return null
        }
    }

}