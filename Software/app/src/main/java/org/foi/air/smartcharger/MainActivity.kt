package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.foi.air.smartcharger.context.Auth
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
    private lateinit var navigationView : NavigationView
    private var currentFragment : String = ""
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
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setCheckedItem(R.id.chargerConnectionItem)

        hamburgerIcon.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

        changeFragment("ChargerConnectionFragment")


    }
    private fun changeFragment(fragmentName: String){
        val fragment = when (fragmentName) {
            "LoginFragment" -> LoginFragment()
            "RegistrationFragment" -> RegistrationFragment()
            "ChargerConnectionFragment" -> ChargerConnectionFragment()
            "RfidListFragment" -> RfidListFragment()
            "ChargingHistoryFragment" -> ChargingHistoryFragment()
            // Add more cases for other fragments as needed
            else -> throw IllegalArgumentException("Unknown fragment name: $fragmentName")
        }

        val fragmentManager = supportFragmentManager

        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment, fragment, fragmentName)

        fragmentTransaction.commit()
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

        if(currentFragment != item.title.toString()){
            when(item.getItemId()){
                R.id.chargerConnectionItem-> changeFragment("ChargerConnectionFragment")
                R.id.registerItem-> changeFragment("RegistrationFragment")
                R.id.loginItem-> changeFragment("LoginFragment")
                R.id.rfidCardsItem-> changeFragment("RfidListFragment")
                R.id.chargingHistoryItem-> changeFragment("ChargingHistoryFragment")
                R.id.logoutItem-> {
                    Auth.deleteData()
                    changeFragment("LoginFragment")

                }
            }
        }

        currentFragment = item.title.toString()
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}