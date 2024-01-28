package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.models.StopEventBody
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.StopEventResponseBody
import org.foi.air.core.network.RequestHandler

class StopChargingRequestHandler(private val requestBody: StopEventBody): RequestHandler<StopEventResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<StopEventResponseBody>) {
        ApiService.eventService.stopEvent(requestBody)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }
}