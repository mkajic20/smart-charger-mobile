package org.foi.air.core.models

import org.foi.air.core.data_classes.RunningEvent

class StartEventResponseBody (success: Boolean, message: String, val event: RunningEvent): ResponseBody(success, message)