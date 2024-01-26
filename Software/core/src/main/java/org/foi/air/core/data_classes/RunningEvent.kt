package org.foi.air.core.data_classes

data class RunningEvent(
    var id : String,
    var chargerId : Int,
    var startTime: String,
    var endTime: String,
    var volume: String
)
