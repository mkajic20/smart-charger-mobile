package org.foi.air.smartcharger.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import org.foi.air.smartcharger.R

class AddNewRfidCardDialog(context : Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_card_dialog)
    }
}