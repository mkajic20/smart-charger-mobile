package org.foi.air.smartcharger.RegistrationActivity

import ResponseListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.foi.air.api.models.LoginBody
import org.foi.air.api.models.RegistrationBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.api.request_handlers.RegistrationRequestHandler
import org.foi.air.core.network.models.ErrorResponseBody
import org.foi.air.core.network.models.SuccessfulLoginResponseBody
import org.foi.air.core.network.models.SuccessfulRegisterResponseBody
import org.foi.air.smartcharger.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btnRegister = binding.btnRegister
        btnRegister.setOnClickListener{
            val requestBody = RegistrationBody(
                binding.txtFirstName.text.toString(),
                binding.txtLastName.text.toString(),
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )

            val registrationRequestHandler = RegistrationRequestHandler(requestBody)
            btnRegister.isEnabled = false

            registrationRequestHandler.sendRequest(object: ResponseListener<SuccessfulRegisterResponseBody>{

                override fun onSuccessfulResponse(response: SuccessfulRegisterResponseBody) {
                    btnRegister.isEnabled = true

                    val loginBody = LoginBody(
                        response.user.email,
                        binding.txtPassword.text.toString()
                    )
                    Log.i("WUBADUBADU","Success")
                    Log.i("WUBADUBADU","Registered user data: "+response.user)

                    loginUser(loginBody)
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    btnRegister.isEnabled = true
                    Log.i("WUBADUBADU","Error message: "+response.error)
                }

                override fun onApiConnectionFailure(t: Throwable) {
                    btnRegister.isEnabled = true
                    Log.i("WUBADUBADU","Error: "+t.toString())
                    Log.i("WUBADUBADU","Api go down")
                }

            })
        }

    }

    private fun loginUser(loginBody: LoginBody) {
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                Log.i("WUBADUBADU","I logged in and imagine this is new page")
                Log.i("WUBADUBADU","And i have this data: "+response.user+" with this jwt: "+response.jwt)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("WUBADUBADU","Error: "+response.error)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("WUBADUBADU","Connection error: "+t.message)

            }

        })
    }
}