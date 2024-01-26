package org.foi.air.smartcharger.context

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.foi.air.api.network.ApiService
import org.foi.air.core.data_classes.UserInfo
import org.json.JSONObject

object Auth {
    private const val FIRST_NAME = "firstName"
    private const val LAST_NAME = "lastName"
    private const val USER_ID = "userId"
    private const val EMAIL = "email"
    private const val JWT = "jwt"
    private var storedUserData : SharedPreferences? = null
    var firstName : String? = ""
    var lastName : String?  = ""
    var userId : String?  = ""
    var email : String? = ""
    private var jwt : String? = ""

    fun initialize(context: Context){
        storedUserData = context.getSharedPreferences("loggedUser", Context.MODE_PRIVATE)
        updateData()
    }

    fun saveUserData(user : UserInfo, jwt : String){
        val userId = getUserId(jwt)
        storedUserData?.edit()?.apply {
            putString(FIRST_NAME, user.firstName)
            putString(LAST_NAME, user.lastName)
            putString(EMAIL, user.email)
            putString(USER_ID, userId)
            putString(JWT, jwt)
            apply()
        }
        updateData()
    }
    private fun updateData(){
        firstName = storedUserData?.getString(FIRST_NAME, "")
        lastName = storedUserData?.getString(LAST_NAME , "")
        userId = storedUserData?.getString(USER_ID, "")
        email = storedUserData?.getString(EMAIL, "")
        jwt = storedUserData?.getString(JWT , "")
        ApiService.authToken = jwt ?: ""
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
