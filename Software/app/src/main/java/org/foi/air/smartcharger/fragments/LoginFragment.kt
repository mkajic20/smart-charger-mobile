package org.foi.air.smartcharger.fragments

import ResponseListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.foi.air.api.models.LoginBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.initialize(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        Auth.initialize(this)
        if(Auth.isLoggedIn()){}
            //changeActivity()
        binding.btnLogin.setOnClickListener{
            val loginBody = LoginBody(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )
            loginUser(loginBody)
        }

        val view = binding.root
        return view
    }
    private fun loginUser(loginBody: LoginBody) {
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                binding.tvEmailError.text = resources.getString(R.string.login_succeeded)
                binding.tvPasswordError.text = resources.getString(R.string.login_succeeded)
                Auth.saveUserData(response.user, response.jwt);
                //if(Auth.isLoggedIn())
                //changeActivity()
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("login","Error: "+response.error)
                errorResponse(response.error)



            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("login","Connection error: "+t.message)
                binding.tvEmailError.text = resources.getString(R.string.cant_reach_server)
                binding.tvPasswordError.text = resources.getString(R.string.cant_reach_server)
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

    /*private fun changeFragment(){
        Log.i("login","Changing activity")
        val intent = Intent(this, dummyLogoutActivity::class.java)
        startActivity(intent)
    }

     */

}