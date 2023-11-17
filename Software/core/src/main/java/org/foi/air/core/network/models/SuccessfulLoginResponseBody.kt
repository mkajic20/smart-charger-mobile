package org.foi.air.core.network.models

import org.foi.air.core.data_classes.UserInfo

class SuccessfulLoginResponseBody (success: Boolean, message: String, val user: UserInfo, val jwt: String): ResponseBody(success, message)