package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.data_classes.RfidCard
import org.foi.air.core.models.CardResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.network.RequestHandler
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyCardRequestHandler(private var cardValue: String): RequestHandler<CardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<CardResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.verifyCard(cardValue)
        serviceCall.enqueue(object: Callback<ResponseBody> {
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

            val cardJson = jsonResponse.getJSONObject("card")
            val userJson = cardJson.getJSONObject("user")

            val rfidCard = RfidCard(
                cardJson.getString("name"),
                cardJson.getString("value"),
                cardJson.getBoolean("active"),
                cardJson.getInt("id"),
                userJson.getInt("id")
            )

            return CardResponseBody(success, message, rfidCard)
        } catch (e: Exception) {
            return null
        }
    }

}