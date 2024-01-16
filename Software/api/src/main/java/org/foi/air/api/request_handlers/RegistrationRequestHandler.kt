package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.models.RegistrationBody
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.network.RequestHandler
import org.foi.air.core.models.RegisterResponseBody

class RegistrationRequestHandler(private val requestBody: RegistrationBody):
    RequestHandler<RegisterResponseBody> {
     override fun sendRequest(responseListener: ResponseListener<RegisterResponseBody>) {
         ApiService.authService.registerUser(requestBody)
             .enqueueWithApiResponse(
                 onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                 onError = { responseListener.onErrorResponse(it.error) },
                 onFailure = { responseListener.onApiConnectionFailure(it)}
             )
    }
}


