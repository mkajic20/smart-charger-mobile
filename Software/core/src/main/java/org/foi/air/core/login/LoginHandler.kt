package org.foi.air.core.login

interface LoginHandler {
    fun handleLogin(email: String, password: String, loginListener: LoginOutcomeListener)
}