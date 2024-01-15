package org.foi.air.smartcharger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.foi.air.core.login.LoginHandler
import org.foi.air.core.login.LoginOutcomeListener
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.LoginResponseBody
import org.foi.air.login_email_password.EmailPasswordLoginHandler
import org.foi.air.login_google.GoogleLoginHandler
import org.foi.air.smartcharger.MainActivity
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var loginHandlers: List<LoginHandler> = listOf(EmailPasswordLoginHandler(), GoogleLoginHandler())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.btnSwitchRegister.setOnClickListener{
            switchToRegisterFragment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginHandlers.forEach { handler ->
            handler.handleLogin(
                this,
                view.findViewById(R.id.login_layout),
                object : LoginOutcomeListener {
                    override fun onSuccessfulLogin(response: LoginResponseBody) {
                        binding.progressBar.visibility = View.INVISIBLE
                        Auth.saveUserData(response.user, response.token)
                        Toast.makeText(this@LoginFragment.context, resources.getString(R.string.login_successful), Toast.LENGTH_LONG).show()
                        (requireActivity() as MainActivity).changeFragment("RfidListFragment")
                        val mainActivity = activity as MainActivity
                        mainActivity.navigationView.setCheckedItem(R.id.rfidCardsItem)
                    }

                    override fun onFailedLogin(response: ErrorResponseBody) {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginFragment.context, resources.getString(R.string.login_unsuccessful), Toast.LENGTH_LONG).show()
                    }

                    override fun onApiConnectionFailure(t: Throwable) {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginFragment.context, resources.getString(R.string.cant_reach_server), Toast.LENGTH_LONG).show()
                    }

                    override fun onButtonClicked() {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                })
        }
    }
    private fun switchToRegisterFragment() {
        (requireActivity() as MainActivity).changeFragment("RegistrationFragment")
        val mainActivity = activity as MainActivity
        mainActivity.navigationView.setCheckedItem(R.id.registerItem)
    }
}