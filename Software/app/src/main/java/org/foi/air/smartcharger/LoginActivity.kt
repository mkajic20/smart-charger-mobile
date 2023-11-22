package org.foi.air.smartcharger

import ResponseListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.foi.air.api.models.LoginBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody
import org.foi.air.smartcharger.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_login)
        binding.btnLogin.setOnClickListener{
            val loginBody = LoginBody(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )
        loginUser(loginBody)


        }

    }
    private fun loginUser(loginBody: LoginBody) {
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                Log.i("login","I logged in and imagine this is new page")
                Log.i("login","And i have this data: "+response.user+" with this jwt: "+response.jwt)
                binding.tvEmailError.text = resources.getString(R.string.login_succeeded)
                binding.tvPasswordError.text = resources.getString(R.string.login_succeeded)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("login","Error: "+response.error)
                errorResponse(response.error)



            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("login","Connection error: "+t.message)

            }

        })
    }

    private fun errorResponse(error : String){
        when (error) {
            "Invalid credentials." -> {
                binding.tvPasswordError.text = resources.getString(R.string.wrong_password)
                binding.tvEmailError.text = ""

            }
            "This account is disabled." ->{
                binding.tvEmailError.text = resources.getString(R.string.disabled_account)
                binding.tvPasswordError.text = ""
            }
            "This email is not registered." ->{
                binding.tvEmailError.text = resources.getString(R.string.unregistered_email)
                binding.tvPasswordError.text = ""
            }

            "Email is not valid." ->{
                binding.tvEmailError.text = resources.getString(R.string.invalid_email)
                binding.tvPasswordError.text = ""
            }
            "Password must have at least 6 characters." ->{
                binding.tvPasswordError.text = resources.getString(R.string.short_password)
                binding.tvEmailError.text = ""
            }
            else -> {
                binding.tvEmailError.text = resources.getString(R.string.unexpected_login_error)
                binding.tvPasswordError.text = resources.getString(R.string.unexpected_login_error)
            }
        }
    }

}