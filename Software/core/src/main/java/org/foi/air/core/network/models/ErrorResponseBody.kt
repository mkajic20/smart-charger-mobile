package org.foi.air.core.network.models

class ErrorResponseBody (success: Boolean, message: String, val error: String): ResponseBody(success, message)