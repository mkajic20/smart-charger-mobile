package org.foi.air.api.network

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.foi.air.core.models.ErrorResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "https://baccboysapi.onrender.com/"
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

    private var retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    val authService: AuthenticationService = retrofitInstance.create(AuthenticationService::class.java)
    val rfidCardService: RfidCardService = retrofitInstance.create(RfidCardService::class.java)
    val eventService: EventService = retrofitInstance.create(EventService::class.java)
    val chargerService: ChargerService = retrofitInstance.create(ChargerService::class.java)

    sealed class ApiResponse<out T> {
        data class Success<out T>(val data: T) : ApiResponse<T>()
        data class Error(val error: ErrorResponseBody) : ApiResponse<Nothing>()
    }

    inline fun <T> handleApiResponse(
        response: Response<T>,
        onSuccess: (ApiResponse.Success<T>) -> Unit,
        onError: (ApiResponse.Error) -> Unit
    ) {
        try {
            if (response.isSuccessful) {
                onSuccess(ApiResponse.Success(response.body()!!))
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()!!.string(), ErrorResponseBody::class.java)
                onError(ApiResponse.Error(errorResponse))
            }
    }catch(e : Exception){
        Log.i("APIconversionError",e.message!!)
    }

    }

    fun <T> Call<T>.enqueueWithApiResponse(
        onSuccess: (ApiResponse.Success<T>) -> Unit,
        onError: (ApiResponse.Error) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                handleApiResponse(response, onSuccess, onError)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}
