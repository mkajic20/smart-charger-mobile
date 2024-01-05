package org.foi.air.login_google

import ResponseListener
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import org.foi.air.api.request_handlers.GoogleAccessTokenRequestHandler
import org.foi.air.api.request_handlers.GoogleLoginRequestHandler
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody

class GoogleLoginHandler :
    LoginHandler {
    private lateinit var fragment: Fragment
    private lateinit var loginButton: MaterialButton
    private lateinit var loginListener: LoginOutcomeListener
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private fun handleGoogleLogin(accessToken: String) {
        val googleLoginRequestHandler = GoogleLoginRequestHandler(accessToken)

        googleLoginRequestHandler.sendRequest(object :
            ResponseListener<SuccessfulLoginResponseBody> {
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                loginListener.onSuccessfulLogin(response)

                // Sign out is here because mGoogleSignInClient is used only during login and not anywhere else
                // Users can now select new account on login screen
                signOut()
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                loginListener.onFailedLogin(response)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                loginListener.onApiConnectionFailure(t)
            }
        })
    }

    fun handleSignInResult(
        completedTask: Task<GoogleSignInAccount>,
        server_client_id: String,
        client_secret: String
    ) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i("success", "ServerAuthCode: ${account.serverAuthCode}")

            val googleAccessTokenRequestHandler = GoogleAccessTokenRequestHandler(
                account.serverAuthCode!!,
                server_client_id,
                client_secret
            )
            googleAccessTokenRequestHandler.requestAccessToken { accessTokenResponse ->
                if (accessTokenResponse != null) {
                    handleGoogleLogin(accessTokenResponse.access_token)
                    Log.i("success", "AccessToken: ${accessTokenResponse.access_token}")
                } else {
                    Log.i("failure", "Failed to get access token.")
                    //loginListener.onFailedLogin("Failed to get access token.")
                }
            }
        } catch (e: ApiException) {
            Log.i("failure", e.toString())
        }
    }

    fun signOut() {
        mGoogleSignInClient?.signOut()
    }

    override fun handleLogin(
        fragment: Fragment,
        login_layout: LinearLayout,
        loginListener: LoginOutcomeListener
    ) {
        this.loginListener = loginListener
        this.fragment = fragment
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.google_login_layout, null)

        val server_client_id = fragment.requireContext().getString(R.string.server_client_id)
        val client_secret = fragment.requireContext().getString(R.string.client_secret)

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(server_client_id)
                .requestServerAuthCode(server_client_id)
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(fragment.requireContext(), gso)

        val launcher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task, server_client_id, client_secret)
                } else {
                    Log.i("failure", "Failed to get response from Google")
                    //loginListener.onFailedLogin("Failed to get response from Google")
                }
            }

        loginButton = view.findViewById(R.id.btnLoginGoogle)
        loginButton.setOnClickListener {
            loginListener.onButtonClicked()
            val signInIntent = mGoogleSignInClient?.signInIntent
            launcher.launch(signInIntent)
        }

        login_layout.addView(view)
    }
}