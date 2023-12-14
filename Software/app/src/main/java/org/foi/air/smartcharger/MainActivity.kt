package org.foi.air.smartcharger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import org.foi.air.smartcharger.context.Auth
import org.foi.air.core.interfaces.OnNewIntentListener
import org.foi.air.smartcharger.databinding.ActivityMainBinding
import org.foi.air.smartcharger.fragments.ChargerConnectionFragment
import org.foi.air.smartcharger.fragments.ChargingHistoryFragment
import org.foi.air.smartcharger.fragments.LoginFragment
import org.foi.air.smartcharger.fragments.RegistrationFragment
import org.foi.air.smartcharger.fragments.RfidListFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var hamburgerIcon: ImageView
    lateinit var navigationView : NavigationView
    private var mOnNewIntentListener: OnNewIntentListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Auth.initialize(this)

        drawerLayout = binding.drawerLayout
        hamburgerIcon = binding.ivHamburger
        navigationView = binding.navigation

        navigationView.setNavigationItemSelectedListener(this)

        navigationView.bringToFront()

        setupDrawerToggle()


        navigationView.setCheckedItem(R.id.chargerConnectionItem)

        updateMenuVisibilityIfUserLoggedIn()

        changeFragment("ChargerConnectionFragment")


    }

    private fun setupDrawerToggle() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun updateMenuVisibilityIfUserLoggedIn(){
        val menu = navigationView.menu
        val header = navigationView.getHeaderView(0)
        hamburgerIcon.setOnClickListener{
            if(Auth.isLoggedIn()){
                menu.findItem(R.id.registerItem).isVisible = false
                menu.findItem(R.id.loginItem).isVisible = false
                menu.findItem(R.id.rfidCardsItem).isVisible = true
                menu.findItem(R.id.chargingHistoryItem).isVisible = true
                menu.findItem(R.id.logoutItem).isVisible = true
                header.findViewById<TextView>(R.id.tvHeaderName).text = getString(R.string.header_name, Auth.firstName, Auth.lastName)
                header.findViewById<TextView>(R.id.tvHeaderEmail).text = "${Auth.email}"

            }else{
                menu.findItem(R.id.registerItem).isVisible = true
                menu.findItem(R.id.loginItem).isVisible = true
                menu.findItem(R.id.rfidCardsItem).isVisible = false
                menu.findItem(R.id.chargingHistoryItem).isVisible = false
                menu.findItem(R.id.logoutItem).isVisible = false
                header.findViewById<TextView>(R.id.tvHeaderName).text = getString(R.string.app_name).uppercase()
                header.findViewById<TextView>(R.id.tvHeaderEmail).text = ""
            }
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }

     fun changeFragment(fragmentName: String){
        val fragment = getFragmentByName(fragmentName)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment, fragmentName)
        fragmentTransaction.commit()
    }

    private fun getFragmentByName(fragmentName : String) : Fragment {
        return when (fragmentName) {
            "LoginFragment" -> LoginFragment()
            "RegistrationFragment" -> RegistrationFragment()
            "ChargerConnectionFragment" -> ChargerConnectionFragment()
            "RfidListFragment" -> RfidListFragment()
            "ChargingHistoryFragment" -> ChargingHistoryFragment()
            else -> throw IllegalArgumentException("Unknown fragment name: $fragmentName")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if(navigationView.checkedItem!!.title != item.title.toString()){
            when(item.getItemId()){
                R.id.chargerConnectionItem-> changeFragment("ChargerConnectionFragment")
                R.id.registerItem-> changeFragment("RegistrationFragment")
                R.id.loginItem-> changeFragment("LoginFragment")
                R.id.rfidCardsItem-> changeFragment("RfidListFragment")
                R.id.chargingHistoryItem-> changeFragment("ChargingHistoryFragment")
                R.id.logoutItem-> {
                    Auth.deleteData()
                    changeFragment("LoginFragment")
                    updateMenuVisibilityIfUserLoggedIn()
                    Handler().postDelayed({
                        navigationView.setCheckedItem(R.id.loginItem)
                    }, 200)
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true}

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mOnNewIntentListener?.newIntent(intent)
    }

    fun setOnNewIntentListener(onNewIntentListener: OnNewIntentListener?){
        mOnNewIntentListener = onNewIntentListener
    }
}