package org.foi.air.smartcharger.context

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.fragment.app.Fragment
import org.foi.air.core.data_classes.UserInfo
import org.json.JSONObject

object Auth {
    private var storedUserData : SharedPreferences? = null
    var firstName : String? = ""
    var lastName : String?  = ""
    var userId : String?  = ""
    var email : String? = ""
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
            putString("email", user.email)
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
        email = storedUserData?.getString("email", "")
        jwt = storedUserData?.getString("jwt" , "")
    }
    fun deleteData(){
        val editor = storedUserData?.edit()
        editor?.clear()
        editor?.apply()
        updateData()
    }
    fun isLoggedIn() : Boolean{
        return firstName != "" && lastName != "" && userId != "" && email != "" && jwt != ""
    }

    private fun getUserId(jwt: String): String{
        val elements = jwt.split('.')
        val payload = elements[1]
        val payloadString = Base64.decode(payload, Base64.DEFAULT).decodeToString()
        return JSONObject(payloadString).optString("userId", "")


    }



}
