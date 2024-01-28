package com.example.nfc_scanner

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.util.Log
import org.foi.air.core.interfaces.NfcHandling

class NfcScanner(private val activity: Activity) : NfcHandling {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private val tagList = mutableListOf<Tag>()

    init {
        initializeNFC()
    }

    private fun initializeNFC() {
        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                activity,
                0,
                Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            )
            nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
        } catch (e: Exception) {
            Log.e("nfcSuperSecretCode", e.toString())
        }
    }

    override fun onResume() {
        pendingIntent ?: run {
            pendingIntent = PendingIntent.getActivity(
                activity, 0,
                Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            )
        }

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    override fun onPause() {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    override fun handleIntent(intent: Intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
            tagList.add(tag)
        }
    }

    override fun getScannedTag(): String? {
        val lastTag = tagList.lastOrNull()
        return lastTag?.let { it ->
            MifareClassic.get(it)?.use { mifare ->
                mifare.tag.id.joinToString(":") { "%02X".format(it) }
            }
        }
    }

    override fun getNfcAdapterStatus(): Boolean {
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) {
            onResume()
            return true
        }
        return false
    }
}