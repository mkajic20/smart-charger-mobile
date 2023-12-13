package org.foi.air.api.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "http://10.0.2.2:5294"


    var authToken : String = ""

    private val interceptor = Interceptor {
        chain ->
        val original = chain.request()

        val request = original.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .method(original.method, original.body)
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private var instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()



    val authService: AuthenticationService = instance.create(AuthenticationService::class.java)
    val rfidCardService: RfidCardService = instance.create(RfidCardService::class.java)
}