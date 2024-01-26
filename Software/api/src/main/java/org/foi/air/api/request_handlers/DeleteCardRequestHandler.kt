package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.network.RequestHandler
import org.foi.air.core.models.ResponseBody


class DeleteCardRequestHandler (private var userId : Int, private var cardId : Int): RequestHandler<ResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<ResponseBody>) {
        ApiService.rfidCardService.deleteCard(userId, cardId)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }

}