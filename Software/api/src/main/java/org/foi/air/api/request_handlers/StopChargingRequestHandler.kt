package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.models.StopEventBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.StopEventResponseBody
import org.foi.air.core.network.RequestHandler
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StopChargingRequestHandler(private val requestBody: StopEventBody): RequestHandler<StopEventResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<StopEventResponseBody>) {
        val service = ApiService.eventService
        val serviceCall = service.stopEvent(requestBody)

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

    private fun successfulResponseConverter(responseBody: ResponseBody): StopEventResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            return StopEventResponseBody(success, message, )
        } catch (e: Exception) {
            return null
        }
    }

}