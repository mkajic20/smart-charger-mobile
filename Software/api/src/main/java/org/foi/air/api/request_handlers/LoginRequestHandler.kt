package org.foi.air.api.request_handlers

import ResponseListener
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.foi.air.api.models.LoginBody
import org.foi.air.api.network.ApiService
import org.foi.air.core.data_classes.UserInfo
import org.foi.air.core.network.RequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRequestHandler(private val requestBody: LoginBody): RequestHandler<SuccessfulLoginResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<SuccessfulLoginResponseBody>){
        val service = ApiService.authService
        val serviceCall = service.loginUser(requestBody)

        serviceCall.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val convertedResponse = successfulResponseConverter(response.body()!!)
                    if(convertedResponse == null) {

                        val errorMessage = "Unsuccessful login, please try again!"
                        responseListener.onErrorResponse(ErrorResponseBody(false,errorMessage,errorMessage))
                    }
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

    private fun successfulResponseConverter(responseBody: ResponseBody): SuccessfulLoginResponseBody? {
        try {
            val jsonResponse = JSONObject(responseBody.string())

            val success = jsonResponse.getBoolean("success")
            val message = jsonResponse.getString("message")

            val userDataJson = jsonResponse.getJSONObject("user")
            val userInfo = UserInfo(
                userDataJson.getString("firstName"),
                userDataJson.getString("lastName"),
                userDataJson.getString("email")
            )

            val token = jsonResponse.getString("token")

            return SuccessfulLoginResponseBody(success, message, userInfo, token)
        } catch (e: Exception) {
            return null
        }
    }
}