package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.RfidCardResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.foi.air.core.models.ErrorResponseBody


class GetRfidCardsForUserRequestHandler (private var userId : Int): RequestHandler<RfidCardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<RfidCardResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.getAllCards(userId)
        serviceCall.enqueue(object: Callback<RfidCardResponseBody>{
            override fun onResponse(call: Call<RfidCardResponseBody>, response: Response<RfidCardResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = RfidCardResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.cards
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

            override fun onFailure(call: Call<RfidCardResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }

}