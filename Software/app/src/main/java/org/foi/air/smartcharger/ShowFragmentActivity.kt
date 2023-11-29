package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.foi.air.smartcharger.databinding.ActivityMainBinding
import org.foi.air.smartcharger.databinding.ActivityShowFragmentBinding
import org.foi.air.smartcharger.fragments.RfidListFragment

class ShowFragmentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShowFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShowFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_fragment)

        val fragment = RfidListFragment()

        // Get the FragmentManager
        val fragmentManager = supportFragmentManager

        // Start a FragmentTransaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the content of the container with the fragment
        fragmentTransaction.replace(R.id.fragmentContainerView2, fragment, "RfidListFragment")

        // Add the transaction to the back stack (optional)
        // fragmentTransaction.addToBackStack("YourFragmentTag")

        // Commit the transaction
        fragmentTransaction.commit()



    }
}