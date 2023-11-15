package org.foi.air.smartcharger.RegistrationActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.foi.air.api.models.RegistrationBody
import org.foi.air.api.request_handlers.RegistrationRequestHandler
import org.foi.air.core.network.RegisterResponseListener
import org.foi.air.core.network.models.ErrorResponseBody
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

            registrationRequestHandler.sendRequest(object: RegisterResponseListener{

                override fun onSuccessfulResponse(response: SuccessfulRegisterResponseBody) {
                    btnRegister.isEnabled = true
                    Log.i("WUBADUBADU","Success")
                    Log.i("WUBADUBADU","User data: "+response.user)
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    btnRegister.isEnabled = true
                    Log.i("WUBADUBADU","Error message: "+response.error)
                }

                override fun onApiConnectionFailure(t: Throwable) {
                    btnRegister.isEnabled = true
                    Log.i("WUBADUBADU","Api go down")
                }

            })
        }

    }
}