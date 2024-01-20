package org.foi.air.core.data_classes

data class RfidCard (
    var name: String,
    var value: String,
    var active: Boolean,
    var id: Int,
    var user: UserInfo
)