package org.foi.air.core.models

import org.foi.air.core.data_classes.RfidCard

class CardResponseBody (success: Boolean, message: String, val rfidCard : RfidCard): ResponseBody(success, message){

}