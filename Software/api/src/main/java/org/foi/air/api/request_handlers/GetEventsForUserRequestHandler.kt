package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.EventsResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetEventsForUserRequestHandler(private var userId : Int, private var page : Int): RequestHandler<EventsResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<EventsResponseBody>) {
        val service = ApiService.eventService
        val serviceCall = service.getEvents(userId, page)
        serviceCall.enqueue(object: Callback<EventsResponseBody>{
            override fun onResponse(call: Call<EventsResponseBody>, response: Response<EventsResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = EventsResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.events
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

            override fun onFailure(call: Call<EventsResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }


        })
    }

}