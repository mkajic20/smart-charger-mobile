package org.foi.air.api.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "http://local_ip_address:5294"
    //private const val BASE_URL = "https://local_ip_address:7024"


    private var instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthenticationService = instance.create(AuthenticationService::class.java)
}