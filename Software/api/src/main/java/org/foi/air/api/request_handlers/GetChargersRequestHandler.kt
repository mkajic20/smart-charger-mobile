package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.ChargersResponseBody
import org.foi.air.core.network.RequestHandler


class GetChargersRequestHandler(private var page : Int): RequestHandler<ChargersResponseBody>{
    override fun sendRequest(responseListener: ResponseListener<ChargersResponseBody>) {
        ApiService.chargerService.getChargers(page)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }

}