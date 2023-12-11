package org.foi.air.smartcharger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.foi.air.smartcharger.databinding.ActivityShowFragmentBinding
import org.foi.air.smartcharger.fragments.LoginFragment
import org.foi.air.smartcharger.fragments.RfidListFragment

class showFragment : AppCompatActivity() {
    private lateinit var binding : ActivityShowFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShowFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_fragment)




    }
}