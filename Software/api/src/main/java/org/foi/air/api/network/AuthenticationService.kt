package org.foi.air.api.network

import okhttp3.ResponseBody
import org.foi.air.api.models.LoggedInUserData
import org.foi.air.api.models.LoginBody
import org.foi.air.api.models.RegistrationBody
import org.foi.air.core.network.models.SuccessfulResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("/api/register")
    fun registerUser(@Body registrationBody: RegistrationBody): Call<ResponseBody>

    @POST("/api/login")
    fun loginUser(@Body loginBody: LoginBody): Call<SuccessfulResponseBody<LoggedInUserData>>
}