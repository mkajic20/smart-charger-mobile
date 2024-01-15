package org.foi.air.api.request_handlers

import ResponseListener
import android.util.Log
import com.google.gson.Gson
import org.foi.air.api.network.ApiService
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.LoginResponseBody
import org.foi.air.core.network.RequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleLoginRequestHandler(private val accessToken: String): RequestHandler<LoginResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<LoginResponseBody>){
        val service = ApiService.authService
        val serviceCall = service.googleLoginUser(accessToken)

        serviceCall.enqueue(object : Callback<LoginResponseBody> {
            override fun onResponse(call: Call<LoginResponseBody>, response: Response<LoginResponseBody>) {
                if(response.isSuccessful){
                    try{
                        val responseSuccess = LoginResponseBody(
                            response.body()!!.success,
                            response.body()!!.message,
                            response.body()!!.user,
                            response.body()!!.token
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

            override fun onFailure(call: Call<LoginResponseBody>, t: Throwable) {
                responseListener.onApiConnectionFailure(t)
            }

        })
    }
}