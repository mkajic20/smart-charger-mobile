package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.ResponseBody


class DeleteCardRequestHandler (private var userId : Int, private var cardId : Int): RequestHandler<ResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<ResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.deleteCard(userId, cardId)
        serviceCall.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = ResponseBody(
                            response.body()!!.success,
                            response.body()!!.message
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

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }


        })
    }

}