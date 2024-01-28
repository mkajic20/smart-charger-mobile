package org.foi.air.core.models

import org.foi.air.core.data_classes.ChargerInfo

class ChargersResponseBody(success: Boolean, message: String, val chargers : MutableList<ChargerInfo>): ResponseBody(success, message)