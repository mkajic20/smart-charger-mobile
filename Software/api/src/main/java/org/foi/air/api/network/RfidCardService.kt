package org.foi.air.api.network

import okhttp3.ResponseBody
import org.foi.air.api.models.NewRfidCardBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RfidCardService {

    @GET("/users/{userId}/cards")
    fun getAllCards(@Path("userId") userId: Int): Call<ResponseBody>
    @POST("/users/{userId}/cards")
    fun createCard(@Path("userId") userId: Int, @Body rfidCard: NewRfidCardBody): Call<ResponseBody>

    @GET("/users/{userId}/cards/{cardId}")
    fun getCard(@Path("userId") userId: Int, @Path("cardId") cardId: Int): Call<ResponseBody>

    @DELETE("/users/{userId}/cards/{cardId}")
    fun deleteCard(@Path("userId") userId: Int, @Path("cardId") cardId: Int): Call<ResponseBody>

}