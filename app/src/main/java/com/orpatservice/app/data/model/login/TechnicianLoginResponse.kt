package com.orpatservice.app.data.model.login

import com.google.gson.annotations.SerializedName

data class TechnicianLoginResponse (val success: Boolean,
                         val data: TechnicianLoginBaseData,
                         val message: String)

data class TechnicianLoginBaseData(
    @SerializedName("token") val token: String,
    @SerializedName("technician") val technician: TechnicianResponse? = null
)



data class TechnicianResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("status") var status: Int? = null
)
