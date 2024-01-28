package org.foi.air.api.models

data class RegistrationBody(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)