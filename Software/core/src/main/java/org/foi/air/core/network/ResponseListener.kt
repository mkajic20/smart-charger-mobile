package org.foi.air.core.network

import org.foi.air.core.network.models.ErrorResponseBody
import org.foi.air.core.network.models.SuccessfulResponseBody

interface ResponseListener {
    fun <T> onSuccessfulResponse(response: SuccessfulResponseBody<T>)
    fun onErrorResponse(response: ErrorResponseBody)
    fun onApiConnectionFailure(t: Throwable)
}