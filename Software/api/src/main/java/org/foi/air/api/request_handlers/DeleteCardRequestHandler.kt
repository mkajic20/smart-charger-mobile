package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.json.JSONObject


class DeleteCardRequestHandler (private var userId : Int, private var cardId : Int): RequestHandler<org.foi.air.core.models.ResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<org.foi.air.core.models.ResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.deleteCard(userId, cardId)
        serviceCall.enqueue(object: Callback<ResponseBody>{
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

    private fun successfulResponseConverter(responseBody: ResponseBody): org.foi.air.core.models.ResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            return org.foi.air.core.models.ResponseBody(success, message)
        } catch (e: Exception) {
            return null
        }
    }

}