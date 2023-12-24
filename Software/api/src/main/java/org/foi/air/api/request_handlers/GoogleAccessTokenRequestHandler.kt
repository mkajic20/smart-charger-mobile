package org.foi.air.api.request_handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleApiService {
    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String
    ): Call<AccessTokenResponse>

    companion object {
        private const val BASE_URL = "https://oauth2.googleapis.com/"

        fun create(): GoogleApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(GoogleApiService::class.java)
        }
    }
}

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
)

class GoogleAccessTokenRequestHandler(private val code: String, private val clientId: String, private val clientSecret: String) {

    private val apiService: GoogleApiService = GoogleApiService.create()
    fun requestAccessToken(callback: (AccessTokenResponse?) -> Unit) {
        val call = apiService.getAccessToken(
            code,
            clientId,
            clientSecret,
            "https://developers.google.com/oauthplayground",
            "authorization_code"
        )

        call.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}