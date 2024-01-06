package org.foi.air.core.login

import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody

interface LoginOutcomeListener {
    fun onSuccessfulLogin(response: SuccessfulLoginResponseBody)
    fun onFailedLogin(response: ErrorResponseBody)
    fun onApiConnectionFailure(t: Throwable)
    fun onButtonClicked()
}