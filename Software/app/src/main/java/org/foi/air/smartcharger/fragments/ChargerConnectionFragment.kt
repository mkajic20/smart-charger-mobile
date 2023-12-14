package org.foi.air.smartcharger.fragments
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.nfc_scanner.NfcScanner
import org.foi.air.core.interfaces.OnNewIntentListener
import org.foi.air.smartcharger.MainActivity
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.databinding.FragmentChargerConnectionBinding

class ChargerConnectionFragment : Fragment() {
    private lateinit var binding : FragmentChargerConnectionBinding
    private lateinit var nfcScanner: NfcScanner
    private var isNfcScanningEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity?)!!.setOnNewIntentListener(object : OnNewIntentListener{
            override fun newIntent(intent: Intent?) {
                if(intent != null){
                    fragmentHandleIntent(intent)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChargerConnectionBinding.inflate(layoutInflater)
        nfcScanner = NfcScanner(requireActivity())

        binding.btnConnectionButton.setOnClickListener()
        {
            if(nfcScanner.getNfcAdapterStatus()){
                isNfcScanningEnabled = !isNfcScanningEnabled
                if (isNfcScanningEnabled) {
                    btnCancelBg()
                } else {
                    btnConnectBg()
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.please_active_nfc), Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        nfcScanner.onResume()
    }

    override fun onPause() {
        super.onPause()
        nfcScanner.onPause()
    }

    fun fragmentHandleIntent(intent: Intent){
        if(isNfcScanningEnabled){
            nfcScanner.handleIntent(intent)
        }
    }

    private fun btnCancelBg(){
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_buttonbackground_cancel)
        binding.btnConnectionButton.text = resources.getString(R.string.cancel_button_text)
        binding.tvStatus.text = resources.getString(R.string.status_waiting_card)
        binding.tvInstructions.text = resources.getString(R.string.instructions_rfid_connection)
    }

    private fun btnConnectBg(){
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_button)
        binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
        binding.tvInstructions.text = resources.getString(R.string.instructions_before_connection)
        binding.tvStatus.text = ""
    }
}