package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.foi.air.smartcharger.databinding.ActivityMainBinding
import org.foi.air.smartcharger.databinding.ActivityShowFragmentBinding
import org.foi.air.smartcharger.fragments.ChargerConnectionFragment
import org.foi.air.smartcharger.fragments.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var hamburgerIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        hamburgerIcon = binding.ivHamburger

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        hamburgerIcon.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

        //val fragment = RfidListFragment()
        val fragment = ChargerConnectionFragment()

        // Get the FragmentManager
        val fragmentManager = supportFragmentManager

        // Start a FragmentTransaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the content of the container with the fragment
        //fragmentTransaction.replace(R.id.fragment, fragment, "RfidListFragment")
        fragmentTransaction.replace(R.id.fragment, fragment, "ChargerConnectionFragment")

        // Add the transaction to the back stack (optional)
        // fragmentTransaction.addToBackStack("YourFragmentTag")

        // Commit the transaction
        fragmentTransaction.commit()

    }
}