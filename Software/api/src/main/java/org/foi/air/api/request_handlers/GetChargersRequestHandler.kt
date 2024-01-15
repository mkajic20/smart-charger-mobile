package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.ChargersResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetChargersRequestHandler(private var page : Int): RequestHandler<ChargersResponseBody>{
    override fun sendRequest(responseListener: ResponseListener<ChargersResponseBody>) {
        val service = ApiService.chargerService
        val serviceCall = service.getChargers(page)
        serviceCall.enqueue(object: Callback<ChargersResponseBody>{
            override fun onResponse(call: Call<ChargersResponseBody>, response: Response<ChargersResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = ChargersResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.chargers
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

            override fun onFailure(call: Call<ChargersResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }

}