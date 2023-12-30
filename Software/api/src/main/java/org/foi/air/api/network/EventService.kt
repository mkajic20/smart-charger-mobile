package org.foi.air.api.network

import okhttp3.ResponseBody
import org.foi.air.api.models.StartEventBody
import org.foi.air.api.models.StopEventBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {

    @POST("/api/events/start")
    fun startEvent(@Body startEventBody: StartEventBody): Call<ResponseBody>
    @PATCH("/api/events/stop")
    fun stopEvent(@Body stopEventBody: StopEventBody): Call<ResponseBody>

    @GET("api/users/{userId}/history")
    fun getEvents(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20
    ): Call<ResponseBody>


}