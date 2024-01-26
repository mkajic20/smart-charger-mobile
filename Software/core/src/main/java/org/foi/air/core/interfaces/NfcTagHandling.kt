package org.foi.air.core.interfaces

import android.content.Intent

interface NfcTagHandling {
    fun handleIntent(intent: Intent)
    fun getScannedTag(): String?
}