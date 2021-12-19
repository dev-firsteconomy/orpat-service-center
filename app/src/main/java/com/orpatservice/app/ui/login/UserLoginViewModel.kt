package com.orpatservice.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.otp.OTPSendResponse
import com.orpatservice.app.ui.data.remote.ApiClient
import com.orpatservice.app.ui.data.remote.ErrorUtils
import com.orpatservice.app.ui.data.repository.DataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ajay Yadav on 15/12/21.
 */
class UserLoginViewModel: ViewModel() {
    val OTPData = MutableLiveData<Resource<OTPSendResponse>>()

    //API to get OTP on user register mobile number
    fun hitOTPApi(mobileNumber: String) {
        DataRepository.instance.hitOTPApi(mobileNumber)
            .enqueue(object : Callback<OTPSendResponse> {
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
            })
    }
}