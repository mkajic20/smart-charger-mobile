package org.foi.air.core.network

import org.foi.air.core.network.models.ErrorResponseBody
import org.foi.air.core.network.models.SuccessfulRegisterResponseBody

interface RegisterResponseListener {
    fun onSuccessfulResponse(response: SuccessfulRegisterResponseBody)
    fun onErrorResponse(response: ErrorResponseBody)
    fun onApiConnectionFailure(t: Throwable)
}