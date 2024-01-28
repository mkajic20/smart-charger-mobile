package org.foi.air.api.network

import org.foi.air.api.models.LoginBody
import org.foi.air.api.models.RegistrationBody
import org.foi.air.core.models.LoginResponseBody
import org.foi.air.core.models.RegisterResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthenticationService {
    @POST("/api/register")
    fun registerUser(@Body registerBody: RegistrationBody): Call<RegisterResponseBody>
    @POST("/api/login")
    fun loginUser(@Body loginBody: LoginBody): Call<LoginResponseBody>
    @POST("/api/login/google")
    fun googleLoginUser(@Body accessToken: String): Call<LoginResponseBody>


}