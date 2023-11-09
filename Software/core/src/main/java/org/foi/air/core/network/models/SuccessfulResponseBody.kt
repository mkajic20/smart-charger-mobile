package org.foi.air.core.network.models

class SuccessfulResponseBody <T>(success: Boolean, message: String, val data: T): ResponseBody(success, message)