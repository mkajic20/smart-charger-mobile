package org.foi.air.core.login

interface LoginHandler {
    fun handleLogin(email: String? = null, password: String? = null, loginListener: LoginOutcomeListener)
}