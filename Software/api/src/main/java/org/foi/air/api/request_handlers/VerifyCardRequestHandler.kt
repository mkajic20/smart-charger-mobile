package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.CardResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyCardRequestHandler(private var cardValue: String): RequestHandler<CardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<CardResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.verifyCard(cardValue)
        serviceCall.enqueue(object: Callback<CardResponseBody> {
            override fun onResponse(call: Call<CardResponseBody>, response: Response<CardResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = CardResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.card
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

            override fun onFailure(call: Call<CardResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }

}