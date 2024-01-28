package org.foi.air.api.models

data class StartEventBody(
    val startTime: String,
    val cardId: String,
    val chargerId: String,
    val userId: String

)