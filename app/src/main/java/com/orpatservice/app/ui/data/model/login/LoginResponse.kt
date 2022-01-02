package com.orpatservice.app.ui.data.model.login

import com.google.gson.annotations.SerializedName

/**
 * Created by Ajay Yadav on 19/12/21.
 */
data class LoginResponse(val success: Boolean, val data: LoginBaseData, val message: String)

data class LoginBaseData(
    @SerializedName("token") val token: String,
    @SerializedName("serviceCenter") val serviceCenter: ServiceCenter,
    @SerializedName("technician") val technician: Technician
)

data class ServiceCenter(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String
)

data class Technician(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("status") var status: Int? = null
)
