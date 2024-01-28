package org.foi.air.api.network

import org.foi.air.core.models.ChargersResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChargerService {
    @GET("api/chargers")
    fun getChargers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ChargersResponseBody>
}