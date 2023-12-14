package com.example.nfc_scanner

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.util.Log
import android.widget.Toast
import org.foi.hr.nfc_scanner.R

class NfcScanner(private val activity: Activity) {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    private val tagList = mutableListOf<Tag>()

    init {
        initializeNFC()
    }

    private fun initializeNFC(){
        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity)

            if(nfcAdapter?.isEnabled!!){
                Toast.makeText(activity.baseContext,
                    activity.baseContext.getString(R.string.this_device_doesn_t_support_nfc),Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(activity, javaClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(activity,0,intent,
                PendingIntent.FLAG_MUTABLE)
            if(nfcAdapter!=null){
                nfcAdapter?.enableForegroundDispatch(activity,pendingIntent,null,null)
            }
        } catch (e: Exception){
            Log.e("nfcSuperSecretCode",e.toString())
        }
    }

    fun onResume() {
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getActivity(
                activity, 0, Intent(activity, activity.javaClass).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }, PendingIntent.FLAG_MUTABLE
            )
        }

        if (nfcAdapter != null) {
            nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
        }
    }

    fun onPause() {
        if (nfcAdapter != null) {
            nfcAdapter?.disableForegroundDispatch(activity)
        }
    }

    fun handleIntent(intent: Intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
            tagList.add(tag)
        }
    }

    fun getScannedTag(): String? {
        val lastTag = tagList.lastOrNull()
        if (tagList.isNotEmpty()) {
            val result = MifareClassic.get(lastTag)?.use { mifare ->
                mifare.tag.id.joinToString(":") { "%02X".format(it) }
            }
            return result
        }
        return null
    }

    fun getNfcAdapterStatus(): Boolean {
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) return true
        return false
    }
}