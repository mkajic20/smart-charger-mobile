package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.models.StartEventBody
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.StartEventResponseBody
import org.foi.air.core.network.RequestHandler

class StartChargingRequestHandler(private val requestBody: StartEventBody): RequestHandler<StartEventResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<StartEventResponseBody>) {
        ApiService.eventService.startEvent(requestBody)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }
}