package org.foi.air.smartcharger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.foi.air.core.interfaces.OnNewIntentListener
import org.foi.air.smartcharger.databinding.ActivityMainBinding
import org.foi.air.smartcharger.fragments.HomepageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var mOnNewIntentListener: OnNewIntentListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, HomepageFragment())
                .commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mOnNewIntentListener?.newIntent(intent)
    }

    fun setOnNewIntentListener(onNewIntentListener: OnNewIntentListener?){
        mOnNewIntentListener = onNewIntentListener
    }
}