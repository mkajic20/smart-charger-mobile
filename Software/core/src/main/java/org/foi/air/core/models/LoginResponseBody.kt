package org.foi.air.core.models

import org.foi.air.core.data_classes.UserInfo

class LoginResponseBody (success: Boolean, message: String, val user: UserInfo, val token: String): ResponseBody(success, message)