package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.core.network.RequestHandler
import org.foi.air.api.models.NewRfidCardBody
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.CardResponseBody

class CreateCardRequestHandler(private val userId: Int, private val requestBody: NewRfidCardBody) :
    RequestHandler<CardResponseBody> {

    override fun sendRequest(responseListener: ResponseListener<CardResponseBody>) {
        ApiService.rfidCardService.createCard(userId, requestBody)
            .enqueueWithApiResponse(
            onSuccess = { responseListener.onSuccessfulResponse(it.data) },
            onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
        )
    }
}