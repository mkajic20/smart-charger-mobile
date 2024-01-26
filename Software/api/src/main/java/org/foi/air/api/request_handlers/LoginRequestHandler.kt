package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.models.LoginBody
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.network.RequestHandler
import org.foi.air.core.models.LoginResponseBody

class LoginRequestHandler(private val requestBody: LoginBody): RequestHandler<LoginResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<LoginResponseBody>){
        ApiService.authService.loginUser(requestBody)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }

}