package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.models.StartEventBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.StartEventResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartChargingRequestHandler(private val requestBody: StartEventBody): RequestHandler<StartEventResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<StartEventResponseBody>) {
        val service = ApiService.eventService
        val serviceCall = service.startEvent(requestBody)

        serviceCall.enqueue(object : Callback<StartEventResponseBody> {
            override fun onResponse(call: Call<StartEventResponseBody>, response: Response<StartEventResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = StartEventResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.event
                        )
                        responseListener.onSuccessfulResponse(responseSuccess)
                    }catch (e: Exception){
                        Log.i("JsonError", "Something went wrong while reading JSON data: " + e.message!!)
                    }
                }else{
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), ErrorResponseBody::class.java)
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<StartEventResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }
}