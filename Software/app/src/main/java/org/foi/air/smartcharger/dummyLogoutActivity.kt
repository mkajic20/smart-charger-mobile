package org.foi.air.smartcharger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.foi.air.smartcharger.databinding.ActivityDummyLogoutBinding
import org.foi.air.smartcharger.databinding.ActivityLoginBinding

class dummyLogoutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDummyLogoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDummyLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storedUserData = getSharedPreferences("loggedUser", Context.MODE_PRIVATE)
        val userId = storedUserData.getString("userId", "doesn't exist")
        val jwt = storedUserData.getString("jwt" , "doesn't exist")
        binding.tvUserId.text = "User id: " + userId
        binding.tvJwt.text = "jwt: " + jwt

        binding.btnLogout.setOnClickListener{
            val editor = storedUserData.edit()
            editor.remove("userId")
            editor.remove("jwt")
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}