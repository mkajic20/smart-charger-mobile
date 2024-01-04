package org.foi.air.login_email_password

import ResponseListener
import android.util.Log
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import org.foi.air.api.models.LoginBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody

class EmailPasswordLoginHandler : LoginHandler {
    /*override fun handleLogin(email: String?, password: String?, loginListener: LoginOutcomeListener) {
        val loginBody = LoginBody(email!!, password!!)
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                loginListener.onSuccessfulLogin(response)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                loginListener.onFailedLogin(response)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                loginListener.onApiConnectionFailure(t)
            }
        })
    }*/

    override fun handleLogin(
        fragment: Fragment,
        login_layout: LinearLayout,
        loginListener: LoginOutcomeListener
    ) {
        TODO("Not yet implemented")
    }
}