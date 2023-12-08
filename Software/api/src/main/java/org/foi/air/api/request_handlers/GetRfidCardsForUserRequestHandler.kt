package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.RfidCardResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.foi.air.core.data_classes.RfidCard
import org.foi.air.core.models.ErrorResponseBody
import org.json.JSONObject

class GetRfidCardsForUserRequestHandler (private var userId : Int): RequestHandler<RfidCardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<RfidCardResponseBody>) {
        val service = ApiService.rfidCardService
        val serviceCall = service.getAllCards(userId)
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

    private fun successfulResponseConverter(responseBody: ResponseBody): RfidCardResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            val rfidCardArray = jsonResponse.getJSONArray("cards")
            val rfidCardList = mutableListOf<RfidCard>()
            for (i in 0 until rfidCardArray.length()) {
                val rfidCardDataJson = rfidCardArray.getJSONObject(i)
                val rfidCardElement = RfidCard(
                    rfidCardDataJson.getString("name"),
                    rfidCardDataJson.getString("value"),
                    rfidCardDataJson.getBoolean("active"),
                    rfidCardDataJson.getInt("id")

                )
                rfidCardList.add(rfidCardElement)
            }

            return RfidCardResponseBody(success, message, rfidCardList)
        } catch (e: Exception) {
            return null
        }
    }

}