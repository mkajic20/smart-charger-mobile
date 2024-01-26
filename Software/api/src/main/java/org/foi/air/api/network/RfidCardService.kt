package org.foi.air.api.network

import org.foi.air.api.models.NewRfidCardBody
import org.foi.air.core.models.CardResponseBody
import org.foi.air.core.models.ResponseBody
import org.foi.air.core.models.RfidCardResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RfidCardService {

    @GET("/api/users/{userId}/cards")
    fun getAllCards(@Path("userId") userId: Int): Call<RfidCardResponseBody>
    @POST("/api/users/{userId}/cards")
    fun createCard(@Path("userId") userId: Int, @Body rfidCard: NewRfidCardBody): Call<CardResponseBody>
    @DELETE("/api/users/{userId}/cards/{cardId}")
    fun deleteCard(@Path("userId") userId: Int, @Path("cardId") cardId: Int): Call<ResponseBody>
    @GET("/api/cards/{cardValue}/verify")
    fun verifyCard(@Path("cardValue") cardValue: String): Call<CardResponseBody>
}