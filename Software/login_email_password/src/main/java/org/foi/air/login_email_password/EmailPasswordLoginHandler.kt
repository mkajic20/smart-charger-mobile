package org.foi.air.login_email_password

import ResponseListener
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.foi.air.api.models.LoginBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.LoginResponseBody

class EmailPasswordLoginHandler : LoginHandler {
    private lateinit var fragment: Fragment
    private lateinit var loginListener: LoginOutcomeListener
    private lateinit var txtEmail: EditText
    private lateinit var tvEmailError: TextView
    private lateinit var txtPassword: EditText
    private lateinit var tvPasswordError: TextView
    private lateinit var btnLogin: Button
    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", RegexOption.IGNORE_CASE)

    private fun performLogin(email: String, password: String) {
        val loginBody = LoginBody(email, password)
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<LoginResponseBody>{
            override fun onSuccessfulResponse(response: LoginResponseBody) {
                if(fragment.isAdded)
                    loginListener.onSuccessfulLogin(response)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                if(fragment.isAdded) {
                    errorResponse(response.error)
                    loginListener.onFailedLogin(response)
                }
            }

            override fun onApiConnectionFailure(t: Throwable) {
                if(fragment.isAdded)
                    loginListener.onApiConnectionFailure(t)
            }
        })
    }

    fun validateInput(email: String, password: String): Boolean {
        var isValid = true
        if (!emailPattern.matches(email.trim())) {
            tvEmailError.text = fragment.requireContext().getString(R.string.email_is_not_valid)
            isValid = false
        }
        else {
            tvEmailError.text = ""
        }
        if (password.isEmpty()) {
            tvPasswordError.text = fragment.requireContext().getString(R.string.password_is_required)
            isValid = false
        }
        else {
            tvPasswordError.text = ""
        }
        return isValid
    }

    private fun errorResponse(error : String) {
        when (error) {
            "Invalid credentials." -> {
                tvPasswordError.text = fragment.requireContext().getString(R.string.wrong_password)
                tvEmailError.text = ""
            }

            "This account is disabled." -> {
                tvEmailError.text = fragment.requireContext().getString(R.string.disabled_account)
                tvPasswordError.text = ""
            }

            "This email is not registered." -> {
                tvEmailError.text = fragment.requireContext().getString(R.string.unregistered_email)
                tvPasswordError.text = ""
            }

            "Email is not valid." -> {
                tvEmailError.text = fragment.requireContext().getString(R.string.invalid_email)
                tvPasswordError.text = ""
            }

            "Password must have at least 6 characters." -> {
                tvPasswordError.text = fragment.requireContext().getString(R.string.short_password)
                tvEmailError.text = ""
            }

            else -> {
                tvEmailError.text = fragment.requireContext().getString(R.string.unexpected_login_error)
                tvPasswordError.text = fragment.requireContext().getString(R.string.unexpected_login_error)
            }
        }
    }

    override fun handleLogin(
        fragment: Fragment,
        login_layout: LinearLayout,
        loginListener: LoginOutcomeListener
    ) {
        this.loginListener = loginListener
        this.fragment = fragment
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.login_email_password_layout, null)

        txtEmail = view.findViewById(R.id.txtEmail)
        tvEmailError = view.findViewById(R.id.tvEmailError)
        txtPassword = view.findViewById(R.id.txtPassword)
        tvPasswordError = view.findViewById(R.id.tvPasswordError)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            if (validateInput(email,password))
            {
                loginListener.onButtonClicked()
                performLogin(email, password)
            }
        }

        login_layout.addView(view)
    }
}