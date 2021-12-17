package com.orpatservice.app.ui.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.model.otp.OTPSendResponse
import com.orpatservice.app.ui.data.remote.ApiClient
import com.orpatservice.app.ui.data.remote.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Vikas Singh on 13/Dec/2020
 */

class DataRepository {
    companion object {
        val instance = DataRepository()
    }

    fun loadTechnician(): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().getTechnicianAPI()
            .enqueue(object : Callback<TechnicianResponse> {

                override fun onFailure(call: Call<TechnicianResponse>, t: Throwable) {
                    mutableTestData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<TechnicianResponse>,
                    response: Response<TechnicianResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableTestData.value = response.body()?.let { Resource.success(it) }
                    } else {
                        mutableTestData.value =
                            Resource.error(
                                ErrorUtils.getError(
                                    response.errorBody(),
                                    response.code()
                                )
                            )
                    }
                }
            })
        return mutableTestData

    }

    //To get OTP on user register mobile number
    fun getOTP(mobileNumber: String): LiveData<Resource<OTPSendResponse>> {
        val mutableOTPData = MutableLiveData<Resource<OTPSendResponse>>()

        mutableOTPData.value = (Resource.loading(null))

        ApiClient.getAuthApi().getOtpAPI(mobileNumber)
            .enqueue(object : Callback<OTPSendResponse>{
                override fun onResponse(
                    call: Call<OTPSendResponse>,
                    response: Response<OTPSendResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableOTPData.value = response.body()?.let { Resource.success(it) }
                    } else {
                        mutableOTPData.value =
                            Resource.error(
                                ErrorUtils.getError(
                                    response.errorBody(),
                                    response.code()
                                )
                            )
                    }
                }

                override fun onFailure(call: Call<OTPSendResponse>, t: Throwable) {
                    mutableOTPData.value = Resource.error(ErrorUtils.getError(t))
                }

            })

        return mutableOTPData
    }
}