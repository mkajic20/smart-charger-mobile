package org.foi.air.api.network

import org.foi.air.api.models.StartEventBody
import org.foi.air.api.models.StopEventBody
import org.foi.air.core.models.EventsResponseBody
import org.foi.air.core.models.StartEventResponseBody
import org.foi.air.core.models.StopEventResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {

    @POST("/api/events/start")
    fun startEvent(@Body startEventBody: StartEventBody): Call<StartEventResponseBody>
    @PATCH("/api/events/stop")
    fun stopEvent(@Body stopEventBody: StopEventBody): Call<StopEventResponseBody>

    @GET("api/users/{userId}/history")
    fun getEvents(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20
    ): Call<EventsResponseBody>


}