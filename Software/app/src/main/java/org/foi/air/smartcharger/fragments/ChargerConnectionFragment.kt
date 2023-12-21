package org.foi.air.smartcharger.fragments

import ResponseListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.nfc_scanner.NfcScanner
import org.foi.air.api.request_handlers.VerifyCardRequestHandler
import org.foi.air.core.interfaces.OnNewIntentListener
import org.foi.air.core.models.CardResponseBody
import org.foi.air.core.models.ErrorResponseBody
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
                binding.tvStatus.text = resources.getString(R.string.please_active_nfc)
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
            checkCard(nfcScanner.getScannedTag())
        }
    }

    private fun checkCard(cardValue: String?) {
        if(cardValue!=null){
            val verifyCardHandler = VerifyCardRequestHandler(cardValue)
            verifyCardHandler.sendRequest(object: ResponseListener<CardResponseBody>{
                override fun onSuccessfulResponse(response: CardResponseBody) {
                    Log.i("scannedCard", "Value: $cardValue")
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    errorUI()
                }

                override fun onApiConnectionFailure(t: Throwable) {
                    connectionErrorUI(t)
                }

            })
            return
        }
        nullError()
    }

    private fun errorUI(){
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_blue_button)
        binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
        binding.tvInstructions.text = getString(R.string.your_card_is_not_registered)
        binding.tvStatus.setTextColor(resources.getColor(R.color.errorColor))
        binding.tvStatus.text = getString(R.string.status_invalid_card)
        binding.ivCardIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_rfidcard_error))
    }

    private fun connectionErrorUI(t: Throwable) {
        binding.ivCardIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_rfidcard_error))
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_blue_button)
        binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
        binding.tvInstructions.text = t.message
        binding.tvStatus.setTextColor(resources.getColor(R.color.errorColor))
        binding.tvStatus.text = resources.getString(R.string.status_service_unreachable)
    }

    private fun nullError() {
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_blue_button)
        binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
        binding.tvInstructions.text = getString(R.string.this_card_is_not_supported)
        binding.tvStatus.setTextColor(resources.getColor(R.color.errorColor))
        binding.tvStatus.text = getString(R.string.status_invalid_card)
        binding.ivCardIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_rfidcard_error))
    }


    private fun btnCancelBg(){
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_buttonbackground_cancel)
        binding.btnConnectionButton.text = resources.getString(R.string.cancel_button_text)
        binding.tvStatus.text = resources.getString(R.string.status_waiting_card)
        binding.tvInstructions.text = resources.getString(R.string.instructions_rfid_connection)
    }

    private fun btnConnectBg(){
        binding.btnConnectionButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_blue_button)
        binding.btnConnectionButton.text = resources.getString(R.string.connect_button_text)
        binding.tvInstructions.text = resources.getString(R.string.instructions_before_connection)
        binding.tvStatus.text = ""
    }
}