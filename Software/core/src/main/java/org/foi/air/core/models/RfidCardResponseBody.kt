package org.foi.air.core.models

import org.foi.air.core.data_classes.RfidCard

class RfidCardResponseBody(success: Boolean, message: String, val rfidCardList : MutableList<RfidCard>): ResponseBody(success, message) {

}