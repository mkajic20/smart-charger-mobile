package org.foi.air.core.data_classes

data class EventInfo(
    var eventId : String,
    var charger : ChargerInfo,
    var startTime: String,
    var endTime: String,
    var volume: String
)
