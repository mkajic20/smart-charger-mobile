package org.foi.air.core.login

import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.LoginResponseBody

interface LoginOutcomeListener {
    fun onSuccessfulLogin(response: LoginResponseBody)
    fun onFailedLogin(response: ErrorResponseBody)
    fun onApiConnectionFailure(t: Throwable)
    fun onButtonClicked()
}