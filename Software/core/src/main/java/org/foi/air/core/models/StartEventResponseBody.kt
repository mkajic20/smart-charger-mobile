package org.foi.air.core.models

import org.foi.air.core.data_classes.EventInfo

class StartEventResponseBody (success: Boolean, message: String, val event: EventInfo): ResponseBody(success, message)