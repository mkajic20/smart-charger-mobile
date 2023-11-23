package org.foi.air.smartcharger

import ResponseListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import org.foi.air.api.models.LoginBody
import org.foi.air.api.request_handlers.LoginRequestHandler
import org.foi.air.core.data_classes.UserInfo
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.SuccessfulLoginResponseBody
import org.foi.air.smartcharger.databinding.ActivityLoginBinding
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storedUserData = getSharedPreferences("loggedUser", Context.MODE_PRIVATE)
        callDummyActivity(storedUserData)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val loginBody = LoginBody(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )
            loginUser(loginBody, storedUserData)
        }
    }

    private fun loginUser(loginBody: LoginBody, storedUserData: SharedPreferences) {
        val loginRequestHandler = LoginRequestHandler(loginBody)

        loginRequestHandler.sendRequest(object: ResponseListener<SuccessfulLoginResponseBody>{
            override fun onSuccessfulResponse(response: SuccessfulLoginResponseBody) {
                binding.tvEmailError.text = resources.getString(R.string.login_succeeded)
                binding.tvPasswordError.text = resources.getString(R.string.login_succeeded)
                saveUserData(response.user, response.jwt, storedUserData);
                callDummyActivity(storedUserData)
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
    private fun saveUserData(user : UserInfo, jwt : String, storedUserData : SharedPreferences){
        //save data to phone memory with private security level
        val editor = storedUserData.edit()
        val userId = getUserId(jwt)
        editor.apply{
            putString("userId", userId)
            putString("jwt", jwt)
            commit()
        }
    }
    private fun getUserId(jwt: String): String{
        val elements = jwt.split('.')
        val payload = elements[1]
        val payloadString = Base64.decode(payload, Base64.DEFAULT).decodeToString()
        val userId = JSONObject(payloadString).optString("userId", "")
        Log.i("login", "Ovo je dekodirani token - user id:" + userId)
        return userId

    }
    private fun deleteUserData(sharedPreferences: SharedPreferences){
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.remove("jwt")
        editor.apply()
    }
    private fun callDummyActivity(storedUserData : SharedPreferences){
        var userId = storedUserData.getString("userId", "doesn't exist")
        var jwt = storedUserData.getString("jwt" , "doesn't exist")
        Log.i("login",userId + jwt)
        if(userId != "doesn't exist" && jwt != "doesn't exist"){
            val intent = Intent(this, dummyLogoutActivity::class.java)
            startActivity(intent)
        }
    }

}