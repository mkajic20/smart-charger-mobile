package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.foi.air.smartcharger.databinding.ActivityMainBinding
import org.foi.air.smartcharger.fragments.HomepageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.cvFragment, HomepageFragment())
                .commit()
        }
    }
}