package org.foi.air.core.login

data class LoginUserData (
    val firstName: String,
    val lastName: String,
    val email: String,
    val jwt: String
)