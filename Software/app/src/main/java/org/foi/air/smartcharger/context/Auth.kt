package org.foi.air.smartcharger.context

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import org.foi.air.core.data_classes.UserInfo
import org.json.JSONObject

object Auth {
    private var storedUserData : SharedPreferences? = null
    var firstName : String? = ""
    var lastName : String?  = ""
    var userId : String?  = ""
    var jwt : String? = ""

    fun initialize(context: Context){
        storedUserData = context.getSharedPreferences("loggedUser", Context.MODE_PRIVATE)
        updateData()
    }

    fun initialize(context: Fragment){
        storedUserData = context.requireContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE)
        updateData()
    }

    fun saveUserData(user : UserInfo, jwt : String){
        val editor = storedUserData?.edit()
        val userId = getUserId(jwt)
        editor?.apply{
            putString("firstName", user.firstName)
            putString("lastName", user.lastName)
            putString("userId", userId)
            putString("jwt", jwt)
            apply()
        }
        updateData()
    }
    private fun updateData(){
        firstName = storedUserData?.getString("firstName", "")
        lastName = storedUserData?.getString("lastName" , "")
        userId = storedUserData?.getString("userId", "")
        jwt = storedUserData?.getString("jwt" , "")
    }
    fun deleteData(){
        val editor = storedUserData?.edit()
        editor?.clear()
        editor?.apply()
        updateData()
    }
    fun isLoggedIn() : Boolean{
        Log.i("login","podaci su spremljeni...mozda")
        Log.i("login",firstName.toString())
        return firstName != "" && lastName != "" && userId != "" && jwt != ""
    }

    private fun getUserId(jwt: String): String{
        val elements = jwt.split('.')
        val payload = elements[1]
        val payloadString = Base64.decode(payload, Base64.DEFAULT).decodeToString()
        val userId = JSONObject(payloadString).optString("userId", "")
        return userId

    }



}
