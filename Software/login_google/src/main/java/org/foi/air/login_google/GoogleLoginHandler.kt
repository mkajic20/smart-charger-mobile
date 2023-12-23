package org.foi.air.login_google

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
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener

class GoogleLoginHandler (fragment: Fragment, server_client_id: String, client_secret: String) :
    LoginHandler {
    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestIdToken(server_client_id)
        .requestServerAuthCode(server_client_id)
        .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(fragment.requireContext(), gso)

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i("success", "Token: ${account.idToken}")
            Log.i("success", "ServerAuthCode: ${account.serverAuthCode}")
        } catch (e: ApiException) {
            Log.i("failure", e.toString())
        }
    }

    val launcher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        } else {
            Log.i("failure", "Failed to get response from Google")
        }
    }

    override fun handleLogin(
        email: String?,
        password: String?,
        loginListener: LoginOutcomeListener
    ) {
        val signInIntent = mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
}