package org.foi.air.smartcharger

import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class UITests {

    @Test
    fun navigateToLoginFragment(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //Open hamburger menu
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.loginItem)).perform(click())
        //Check if in login fragment
        onView(withId(R.id.bgLoginWave)).check(matches(isDisplayed()))
    }
    @Test
    fun unsuccessfulLogin(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //Open hamburger menu
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.loginItem)).perform(click())
        //Login fragment
        onView(withId(org.foi.air.login_email_password.R.id.txtEmail)).perform(typeText("admin@gmail.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(org.foi.air.login_email_password.R.id.btnLogin)).perform(click())
        //Check if still in login fragment
        onView(withId(R.id.bgLoginWave)).check(matches(isDisplayed()))
    }

    @Test
    fun successfulLogin(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //Open hamburger menu
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.loginItem)).perform(click())
        //Login fragment
        onView(withId(org.foi.air.login_email_password.R.id.txtEmail)).perform(typeText("admin@gmail.com"))
        onView(withId(org.foi.air.login_email_password.R.id.txtPassword)).perform(typeText("test123"))
        Espresso.closeSoftKeyboard()
        onView(withId(org.foi.air.login_email_password.R.id.btnLogin)).perform(click())
        //Wait for login to process
        SystemClock.sleep(2000)
        onView(withId(R.id.tvMainTitleRfidList)).check(matches(isDisplayed()))
        //Logout
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.logoutItem)).perform(click())
    }

    @Test
    fun navigateToChargingHistory(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //Open hamburger menu
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.loginItem)).perform(click())
        //Login fragment
        onView(withId(org.foi.air.login_email_password.R.id.txtEmail)).perform(typeText("admin@gmail.com"))
        onView(withId(org.foi.air.login_email_password.R.id.txtPassword)).perform(typeText("test123"))
        Espresso.closeSoftKeyboard()
        onView(withId(org.foi.air.login_email_password.R.id.btnLogin)).perform(click())
        //Wait for login to process
        SystemClock.sleep(2000)
        //Navigate to charging history fragment
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.chargingHistoryItem)).perform(click())
        //Wait for fragment to load
        SystemClock.sleep(2000)
        onView(withId(R.id.chargerNameTitle)).check(matches(isDisplayed()))
        //Logout
        onView(withId(R.id.ivHamburger)).perform(click())
        onView(withId(R.id.logoutItem)).perform(click())
    }

}