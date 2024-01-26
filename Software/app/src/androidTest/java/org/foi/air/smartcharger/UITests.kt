package org.foi.air.smartcharger

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import org.foi.air.smartcharger.fragments.LoginFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class UITests {
    private lateinit var loginFragment: FragmentScenario<LoginFragment>

    @Test
    fun wrongLogin_applicationWontLoginUser(){
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        loginFragment = launchFragmentInContainer()
        loginFragment.moveToState(Lifecycle.State.STARTED)

        onView(withId(org.foi.air.login_email_password.R.id.txtEmail)).perform(typeText("admin@gmail.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(org.foi.air.login_email_password.R.id.btnLogin)).perform(click())
        assertTrue(uiDevice.hasObject(By.displayId(R.id.tvMainHeader)))
    }
}