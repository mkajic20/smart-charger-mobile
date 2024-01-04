package org.foi.air.core.data_classes

data class ChargerInfo(
    var name : String,
    var latitude : Double,
    var longitude: Double,
    var creationTime: String,
    var active: Boolean,
    var creatorId: Int,
    var id: Int
)
