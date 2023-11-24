package org.foi.air.core.models

import org.foi.air.core.data_classes.UserInfo

class SuccessfulRegisterResponseBody (success: Boolean, message: String, val user: UserInfo): ResponseBody(success, message)