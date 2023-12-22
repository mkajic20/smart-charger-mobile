package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.foi.air.api.models.NewRfidCardBody
import org.foi.air.core.data_classes.RfidCard
import org.foi.air.core.models.CardResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.json.JSONObject

class CreateCardRequestHandler (private var userId : Int, private var requestBody : NewRfidCardBody): RequestHandler<CardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<CardResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.createCard(userId, requestBody)
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

    private fun successfulResponseConverter(responseBody: ResponseBody): CardResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            val rfidCardJson = jsonResponse.getJSONObject("card")
            val userJson = rfidCardJson.getJSONObject("user")

                val rfidCard = RfidCard(
                    rfidCardJson.getString("name"),
                    rfidCardJson.getString("value"),
                    rfidCardJson.getBoolean("active"),
                    rfidCardJson.getInt("id"),
                    userJson.getInt("id")
                )


            return CardResponseBody(success, message, rfidCard)
        } catch (e: Exception) {
            return null
        }
    }

}