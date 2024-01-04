package org.foi.air.core.login

import android.widget.LinearLayout
import androidx.fragment.app.Fragment

interface LoginHandler {
    fun handleLogin(fragment: Fragment, login_layout: LinearLayout, loginListener: LoginOutcomeListener)
}