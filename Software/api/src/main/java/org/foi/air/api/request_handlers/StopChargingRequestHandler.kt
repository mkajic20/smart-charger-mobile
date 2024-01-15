package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.models.StopEventBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.StopEventResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StopChargingRequestHandler(private val requestBody: StopEventBody): RequestHandler<StopEventResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<StopEventResponseBody>) {
        val service = ApiService.eventService
        val serviceCall = service.stopEvent(requestBody)

        serviceCall.enqueue(object : Callback<StopEventResponseBody> {
            override fun onResponse(call: Call<StopEventResponseBody>, response: Response<StopEventResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = StopEventResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
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

            override fun onFailure(call: Call<StopEventResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }
}