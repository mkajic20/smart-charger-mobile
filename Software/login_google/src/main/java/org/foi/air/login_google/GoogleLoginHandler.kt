package org.foi.air.login_google

import ResponseListener
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import org.foi.air.api.request_handlers.GoogleLoginRequestHandler
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody

class GoogleLoginHandler :
    LoginHandler {
    private lateinit var loginButton: MaterialButton
    private lateinit var loginListener: LoginOutcomeListener
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private fun handleGoogleLogin(accessToken: String) {
        val googleLoginRequestHandler = GoogleLoginRequestHandler(accessToken)

        googleLoginRequestHandler.sendRequest(object :
            ResponseListener<SuccessfulLoginResponseBody> {
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                loginListener.onSuccessfulLogin(response)

                // Sign out is here because mGoogleSignInClient is used only during login in this class
                // Users can now select new account on login screen
                mGoogleSignInClient.signOut()
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                loginListener.onFailedLogin(response)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                loginListener.onApiConnectionFailure(t)
            }
        })
    }

    override fun handleLogin(
        fragment: Fragment,
        login_layout: LinearLayout,
        loginListener: LoginOutcomeListener
    ) {
        this.loginListener = loginListener
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.google_login_layout, null)

        val server_client_id = fragment.requireContext().getString(R.string.server_client_id)

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
                    loginListener.onButtonClicked()
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    Log.i("success", "ServerAuthCode: ${task.result.serverAuthCode}")
                    handleGoogleLogin(task.result.serverAuthCode!!)
                } else {
                    loginListener.onFailedLogin(ErrorResponseBody(false, "Result code is not OK", "Google login failed"))
                }
            }

        loginButton = view.findViewById(R.id.btnLoginGoogle)
        loginButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        login_layout.addView(view)
    }
}