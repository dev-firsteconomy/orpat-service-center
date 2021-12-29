package com.orpatservice.app.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.login.LoginResponse
import com.orpatservice.app.ui.data.model.login.OTPSendResponse
import com.orpatservice.app.ui.data.remote.ErrorUtils
import com.orpatservice.app.ui.data.repository.DataRepository
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ajay Yadav on 15/12/21.
 */
class UserLoginViewModel : ViewModel() {
    val OTPData = MutableLiveData<Resource<OTPSendResponse>>()
    val loginData = MutableLiveData<Resource<LoginResponse>>()

    //API to get OTP on user register mobile number
    fun hitOTPApi(mobileNumber: String) {
        if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.ADMIN)) {
            DataRepository.instance.hitServiceCenterOTPApi(mobileNumber)
                .enqueue(callbackGetOTP)
        } else {
            DataRepository.instance.hitTechnicianOTPApi(mobileNumber)
                .enqueue(callbackGetOTP)
        }
    }

    private val callbackGetOTP: Callback<OTPSendResponse> =
        object : Callback<OTPSendResponse> {
            override fun onResponse(
                call: Call<OTPSendResponse>,
                response: Response<OTPSendResponse>
            ) {
                if (response.isSuccessful) {
                    OTPData.value = response.body()?.let { Resource.success(it) }
                } else {
                    OTPData.value =
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        )
                }
            }
            override fun onFailure(call: Call<OTPSendResponse>, t: Throwable) {
                OTPData.value = Resource.error(ErrorUtils.getError(t))
            }
        }

    //API to verify and login
    fun hitVerifyOTPLoginApi(mobileNumber: String, otp: String) {
        if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.ADMIN)) {
            DataRepository.instance.hitVerifyServiceCenterOTPLoginApi(mobileNumber, otp)
                .enqueue(callbackVerifyOTPLogin)
        } else {
            DataRepository.instance.hitVerifyTechnicianOTPLoginApi(mobileNumber, otp)
                .enqueue(callbackVerifyOTPLogin)
        }
    }

    private val callbackVerifyOTPLogin: Callback<LoginResponse> = object : Callback<LoginResponse> {
        override fun onResponse(
            call: Call<LoginResponse>,
            response: Response<LoginResponse>
        ) {
            if (response.isSuccessful) {
                loginData.value = response.body()?.let { Resource.success(it) }
            } else {
                loginData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }
        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            loginData.value = Resource.error(ErrorUtils.getError(t))
        }
    }
}