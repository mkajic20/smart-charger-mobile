package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.CardResponseBody
import org.foi.air.core.network.RequestHandler

class VerifyCardRequestHandler(private var cardValue: String): RequestHandler<CardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<CardResponseBody>) {
        ApiService.rfidCardService.verifyCard(cardValue)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }

}