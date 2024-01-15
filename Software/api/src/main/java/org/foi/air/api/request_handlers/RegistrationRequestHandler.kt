package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.models.RegistrationBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.network.RequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.RegisterResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationRequestHandler(private val requestBody: RegistrationBody):
    RequestHandler<RegisterResponseBody> {
     override fun sendRequest(responseListener: ResponseListener<RegisterResponseBody>) {
        val service = ApiService.authService
        val serviceCall = service.registerUser(requestBody)

        serviceCall.enqueue(object : Callback<RegisterResponseBody>{

            override fun onResponse(call: Call<RegisterResponseBody>, response: Response<RegisterResponseBody>) {
                if (response.isSuccessful) {
                    try{
                        val responseSuccess = RegisterResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.user
                        )
                        responseListener.onSuccessfulResponse(responseSuccess)
                    }catch (e: Exception){
                        Log.i("JsonError", "Something went wrong while reading JSON data: " + e.message!!)
                    }
                }
                else{
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), ErrorResponseBody::class.java)
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<RegisterResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }

}


