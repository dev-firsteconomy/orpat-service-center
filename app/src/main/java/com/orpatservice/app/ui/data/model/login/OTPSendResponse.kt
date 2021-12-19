package com.orpatservice.app.ui.data.model.login

/**
 * Created by Ajay Yadav on 15/12/21.
 */
data class OTPSendResponse(val success: Boolean, val data: OTPBaseData, val message: String)

data class OTPBaseData(val data: OTPData)
