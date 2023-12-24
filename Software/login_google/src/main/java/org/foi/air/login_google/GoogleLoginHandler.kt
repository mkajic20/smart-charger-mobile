package org.foi.air.login_google

import ResponseListener
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.foi.air.api.request_handlers.GoogleAccessTokenRequestHandler
import org.foi.air.api.request_handlers.GoogleLoginRequestHandler
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody

class GoogleLoginHandler (fragment: Fragment, server_client_id: String, client_secret: String) :
    LoginHandler {
    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestIdToken(server_client_id)
        .requestServerAuthCode(server_client_id)
        .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(fragment.requireContext(), gso)
    var loginListener: LoginOutcomeListener? = null
    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, server_client_id: String, client_secret: String) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i("success", "ServerAuthCode: ${account.serverAuthCode}")

            val googleAccessTokenRequestHandler = GoogleAccessTokenRequestHandler(account.serverAuthCode!!, server_client_id, client_secret)
            googleAccessTokenRequestHandler.requestAccessToken { accessTokenResponse ->
                if (accessTokenResponse != null) {
                    handleGoogleLogin(accessTokenResponse.access_token)
                    Log.i("success", "AccessToken: ${accessTokenResponse.access_token}")
                } else {
                    Log.i("failure", "Failed to get access token.")
                }
            }
        } catch (e: ApiException) {
            Log.i("failure", e.toString())
        }
    }

    private fun handleGoogleLogin(accessToken: String) {
        val googleLoginRequestHandler = GoogleLoginRequestHandler(accessToken)

        googleLoginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                loginListener?.onSuccessfulLogin(response)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                loginListener?.onFailedLogin(response)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                loginListener?.onApiConnectionFailure(t)
            }
        })
    }

    val launcher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task, server_client_id, client_secret)
        } else {
            Log.i("failure", "Failed to get response from Google")
        }
    }

        override fun handleLogin(
        email: String?,
        password: String?,
        loginListener: LoginOutcomeListener
    ) {
            this.loginListener = loginListener
        val signInIntent = mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)

        /*val googleLoginRequestHandler = GoogleLoginRequestHandler(accessToken)
        googleLoginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                loginListener.onSuccessfulLogin(response)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                loginListener.onFailedLogin(response)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                loginListener.onApiConnectionFailure(t)
            }
        })*/
    }
}