package com.orpatservice.app.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.login.LoginResponse
import com.orpatservice.app.data.model.login.OTPSendResponse
import com.orpatservice.app.data.remote.ApiEndPoint
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.repository.DataRepository
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ajay Yadav on 15/12/21.
 */
class UserLoginViewModel : ViewModel() {
    val OTPData = MutableLiveData<Resource<OTPSendResponse>>()
    val loginData = MutableLiveData<Resource<LoginResponse>>()
    val otpVerifyData = MutableLiveData<Resource<OTPSendResponse>>()

    fun hitServiceCenterLoginApi(email: String, password: String) {
        DataRepository.instance.hitServiceCenterLoginApi(email, password).enqueue(callbackLogin)
    }
    //API to get OTP on user register mobile number
    fun hitOTPApi(mobileNumber: String) {
       /* if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
        //No need for now, as per discussion Service Center have login by email and password
        //DataRepository.instance.hitServiceCenterOTPApi(mobileNumber).enqueue(callbackGetOTP)
        } else if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {

           DataRepository.instance.hitTechnicianOTPApi(mobileNumber).
           enqueue(callbackGetOTP)
        }
*/

        //if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {

            DataRepository.instance.hitTechnicianOTPApi(mobileNumber).
            enqueue(callbackGetOTP)
      //  }
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
        if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
        //No need for now, as per discussion Service Center have login by email and password
        //DataRepository.instance.hitVerifyServiceCenterOTPLoginApi(mobileNumber, otp).enqueue(callbackVerifyOTPLogin)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)){
            DataRepository.instance.hitVerifyTechnicianOTPLoginApi(mobileNumber, otp)
                .enqueue(callbackLogin)
        }
    }


    private val callbackLogin: Callback<LoginResponse> = object : Callback<LoginResponse> {
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

    private val callbackOtpVarification: Callback<OTPSendResponse> = object : Callback<OTPSendResponse> {
        override fun onResponse(
            call: Call<OTPSendResponse>,
            response: Response<OTPSendResponse>
        ) {
            if (response.isSuccessful) {
                otpVerifyData.value = response.body()?.let { Resource.success(it) }
            } else {
                otpVerifyData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }
        override fun onFailure(call: Call<OTPSendResponse>, t: Throwable) {
            otpVerifyData.value = Resource.error(ErrorUtils.getError(t))
        }
    }
}



/* .enqueue(object : Callback<OTPSendResponse> {
                    override fun onSuccess(
                        call: Callback<OTPSendResponse>,
                        response: Response<OTPSendResponse>
                    ) {

                        response.body()?.success?.let {

                            if (it) {
                                response.body()?.data?.let {

                                }

                            }

                            override fun onError(
                                call: Call<OTPSendResponse>,
                                t: Throwable,
                                response: Response<OTPSendResponse>?
                            ) {
                                response?.let {
//                        verifyOtpMessage.postValue(
//                            DefaultDataModel(
//                            response.isSuccessful,
//                            response.message())
//                        )
                                }
                            }
                        }

                    }
                })
        }
        }*/