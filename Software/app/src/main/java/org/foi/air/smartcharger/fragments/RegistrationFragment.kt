package org.foi.air.smartcharger.fragments

import ResponseListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.foi.air.api.models.LoginBody
import org.foi.air.api.models.RegistrationBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.api.request_handlers.RegistrationRequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody
import org.foi.air.core.models.SuccessfulRegisterResponseBody
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.databinding.FragmentRegistrationBinding
import org.foi.air.smartcharger.validations.RegistrationValidations

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        val view = binding.root

        val btnRegister = binding.btnRegister
        btnRegister.setOnClickListener {

            if(!validateInput(binding.txtFirstName.text.toString(),
                    binding.txtLastName.text.toString(),
                    binding.txtEmail.text.toString(),
                    binding.txtPassword.text.toString(),
                    binding.txtConfirmPassword.text.toString())) return@setOnClickListener

            val requestBody = RegistrationBody(
                binding.txtFirstName.text.toString(),
                binding.txtLastName.text.toString(),
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )

            val registrationRequestHandler = RegistrationRequestHandler(requestBody)
            btnRegister.isEnabled = false

            registrationRequestHandler.sendRequest(object : ResponseListener<SuccessfulRegisterResponseBody>{
                override fun onSuccessfulResponse(response: SuccessfulRegisterResponseBody) {
                    btnRegister.isEnabled = true

                    val loginBody = LoginBody(
                        response.user.email,
                        binding.txtPassword.text.toString()
                    )

                    loginUser(loginBody)
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    btnRegister.isEnabled = true
                    binding.lbErrorMessages.text = response.error
                }

                override fun onApiConnectionFailure(t: Throwable) {
                    btnRegister.isEnabled = true
                    binding.lbErrorMessages.text = t.message
                }
            })
        }

        return view
    }

    private fun loginUser(loginBody: LoginBody) {
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                TODO("Implement navigation to another page")
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                binding.lbErrorMessages.text = response.error
            }

            override fun onApiConnectionFailure(t: Throwable) {
                binding.lbErrorMessages.text = t.message
            }

        })
    }

    private fun validateInput(firstName: String,lastName: String, email: String, password: String, confirmPassword: String): Boolean{
        var validationErrorCounter = 0
        val validations = RegistrationValidations()
        if(!validations.firstNameValidation(firstName)) {
            binding.lbFirstNameValidation.text = getString(R.string.validation_firstname)
            validationErrorCounter++
        } else {
            binding.lbFirstNameValidation.text = ""
        }
        if(!validations.lastNameValidation(lastName)) {
            binding.lbLastNameValidation.text = getString(R.string.validation_lastname)
            validationErrorCounter++
        }else {
            binding.lbLastNameValidation.text = ""
        }
        if(!validations.emailValidation(email)) {
            binding.lbEmailValidation.text = getString(R.string.validation_email)
            validationErrorCounter++
        }else {
            binding.lbEmailValidation.text = ""
        }
        if(!validations.passwordValidation(password)) {
            binding.lbPasswordValidation.text = getString(R.string.validation_password)
            validationErrorCounter++
        }else {
            binding.lbPasswordValidation.text = ""
        }
        if(!validations.confirmPasswordValidation(password,confirmPassword)) {
            binding.lbConfirmPasswordValidation.text =
                getString(R.string.validation_confirm_password)
            validationErrorCounter++
        }else {
            binding.lbConfirmPasswordValidation.text = ""
        }
        if(validationErrorCounter == 0) return true
        return false
    }
}