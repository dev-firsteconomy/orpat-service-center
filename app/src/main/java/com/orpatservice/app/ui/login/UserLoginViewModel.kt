package com.orpatservice.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.otp.OTPSendResponse
import com.orpatservice.app.ui.data.repository.DataRepository

/**
 * Created by Ajay Yadav on 15/12/21.
 */
class UserLoginViewModel: ViewModel() {

    //To get OTP on user register mobile number
    fun getOTP(mobileNumber: String): LiveData<Resource<OTPSendResponse>> {
        return DataRepository.instance.getOTP(mobileNumber)
    }
}