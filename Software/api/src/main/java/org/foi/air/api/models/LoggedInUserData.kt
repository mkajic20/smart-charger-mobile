package org.foi.air.api.models

import com.google.gson.annotations.SerializedName
import org.foi.air.core.data_classes.UserInfo

data class LoggedInUserData (
    @SerializedName("user") var user: Array<UserInfo>,
    @SerializedName("token") var token: String,
    @SerializedName("success") var success: Boolean,
    @SerializedName("message") var message: String
)