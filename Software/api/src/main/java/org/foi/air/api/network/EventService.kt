package org.foi.air.api.network

import okhttp3.ResponseBody
import org.foi.air.api.models.StartEventBody
import org.foi.air.api.models.StopEventBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface EventService {

    @POST("/api/events/start")
    fun startEvent(@Body startEventBody: StartEventBody): Call<ResponseBody>
    @PATCH("/api/events/stop")
    fun stopEvent(@Body stopEventBody: StopEventBody): Call<ResponseBody>

}