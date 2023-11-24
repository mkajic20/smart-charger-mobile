package org.foi.air.core.models

class ErrorResponseBody (success: Boolean, message: String, val error: String): ResponseBody(success, message)