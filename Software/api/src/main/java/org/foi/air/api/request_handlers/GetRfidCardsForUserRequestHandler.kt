package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.RfidCardResponseBody
import org.foi.air.core.network.RequestHandler

class GetRfidCardsForUserRequestHandler (private var userId : Int): RequestHandler<RfidCardResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<RfidCardResponseBody>) {
        ApiService.rfidCardService.getAllCards(userId)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }

}