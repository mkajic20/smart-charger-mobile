package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.LoginResponseBody
import org.foi.air.core.network.RequestHandler

class GoogleLoginRequestHandler(private val accessToken: String): RequestHandler<LoginResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<LoginResponseBody>){
        ApiService.authService.googleLoginUser(accessToken)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }
}