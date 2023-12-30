package org.foi.air.core.models

import org.foi.air.core.data_classes.EventInfo

class EventsResponseBody(success: Boolean, message: String, val eventList : MutableList<EventInfo>): ResponseBody(success, message)