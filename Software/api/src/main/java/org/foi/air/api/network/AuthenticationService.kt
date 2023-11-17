package org.foi.air.api.network

import okhttp3.ResponseBody

import org.foi.air.api.models.LoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthenticationService {
    @POST("/api/login")
    fun loginUser(@Body loginBody: LoginBody): Call<ResponseBody>
}