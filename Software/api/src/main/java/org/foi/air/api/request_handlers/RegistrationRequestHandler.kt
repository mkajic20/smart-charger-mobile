package org.foi.air.api.request_handlers

//import org.foi.air.core.network.models.ResponseBody
import android.util.Log
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.models.RegistrationBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.network.RequestHandler
import org.foi.air.core.network.ResponseListener
import org.foi.air.core.network.models.ErrorResponseBody
import org.foi.air.core.network.models.SuccessfulResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationRequestHandler (private val requestBody: RegistrationBody): RequestHandler {
    override fun sendRequest(responseListener: ResponseListener) {
        Log.i("WUBADUBADU","šaljem.....")
        val service = ApiService.authService
        val serviceCall = service.registerUser(requestBody)
        Log.i("WUBADUBADU","------------------!!!")
        Log.i("WUBADUBADU","service: "+service.toString())
        Log.i("WUBADUBADU","serviceCall:  "+serviceCall.toString())
        Log.i("WUBADUBADU","------------------!!!")

        serviceCall.enqueue(object: Callback<ResponseBody> {


            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i("WUBADUBADU","enqueue")
                Log.i("WUBADUBADU",response.body().toString())
                if(response.isSuccessful){
                    Log.i("WUBADUBADU","---------------------")
                    val gson = Gson()
                    Log.i("WUBADUBADU","response message: "+response.message())
                    Log.i("WUBADUBADU","response body: "+response.body())
                    Log.i("WUBADUBADU","response fdjnsfdjn: "+gson.fromJson(response.body().toString(), ResponseBody::class.java))
                    Log.i("WUBADUBADU","response errorbody: "+response.errorBody())
                    Log.i("WUBADUBADU","---------------------")
                    responseListener.onSuccessfulResponse(response.body() as SuccessfulResponseBody<*>)
                }else{
                    Log.i("WUBADUBADU","enqueu3")
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), ErrorResponseBody:: class.java)
                    Log.i("WUBADUBADU","???--------------???")
                    Log.i("WUBADUBADU","Error message: "+response.errorBody())
                    Log.i("WUBADUBADU","???--------------???")
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i("WUBADUBADU","------------------")
                Log.i("WUBADUBADU","Failed to connect to network: "+call.request())
                Log.i("WUBADUBADU","------------------")
                responseListener.onApiConnectionFailure(t)
            }
        })
    }
}
