package com.orpatservice.app.ui.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.AddTechnicianResponse
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.model.login.LoginResponse
import com.orpatservice.app.ui.data.model.login.OTPSendResponse
import com.orpatservice.app.ui.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.data.remote.ApiClient
import com.orpatservice.app.ui.data.remote.ErrorUtils
import okhttp3.MultipartBody
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

    //To get OTP on user register mobile number
    fun hitServiceCenterOTPApi(mobileNumber: String): Call<OTPSendResponse> {
        return ApiClient.getAuthApi().getServiceCenterOtpAPI(mobileNumber)
    }

    //To get OTP on user register mobile number
    fun hitTechnicianOTPApi(mobileNumber: String): Call<OTPSendResponse> {
        return ApiClient.getAuthApi().getTechnicianOtpAPI(mobileNumber)
    }

    //To verify OTP and login
    fun hitVerifyServiceCenterOTPLoginApi(mobileNumber: String, otp: String): Call<LoginResponse> {
        return ApiClient.getAuthApi().verifyServiceCenterOTPLoginAPI(mobileNumber, otp)
    }

    //To verify OTP and login
    fun hitVerifyTechnicianOTPLoginApi(mobileNumber: String, otp: String): Call<LoginResponse> {
        return ApiClient.getAuthApi().verifyTechnicianOTPLoginAPI(mobileNumber, otp)
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

    fun loadNextPageTechnician(nextPage : Int): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().getNextPageTechnicianAPI(nextPage)
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

    fun hitAPIAddTechnician(
        requestBody: MultipartBody
    ): LiveData<Resource<AddTechnicianResponse>> {
        val mutableLiveData = MutableLiveData<Resource<AddTechnicianResponse>>()

        mutableLiveData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPIAddTechnician(requestBody)
            .enqueue(object : Callback<AddTechnicianResponse> {

                override fun onFailure(call: Call<AddTechnicianResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<AddTechnicianResponse>,
                    response: Response<AddTechnicianResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.value = response.body()?.let { Resource.success(it) }
                    } else {
                        mutableLiveData.value =
                            Resource.error(
                                ErrorUtils.getError(
                                    response.errorBody(),
                                    response.code()
                                )
                            )
                    }
                }
            })
        return mutableLiveData

    }

    fun hitAPIUpdateTechnician(
        requestBody: MultipartBody,
        technicianID: Int?
    ): LiveData<Resource<AddTechnicianResponse>> {
        val mutableLiveData = MutableLiveData<Resource<AddTechnicianResponse>>()

        mutableLiveData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPIUpdateTechnician(requestBody,technicianID)
            .enqueue(object : Callback<AddTechnicianResponse> {

                override fun onFailure(call: Call<AddTechnicianResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<AddTechnicianResponse>,
                    response: Response<AddTechnicianResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.value = response.body()?.let { Resource.success(it) }
                    } else {
                        mutableLiveData.value =
                            Resource.error(
                                ErrorUtils.getError(
                                    response.errorBody(),
                                    response.code()
                                )
                            )
                    }
                }
            })
        return mutableLiveData
    }

    //Lead API
    fun hitGetServiceCenterPendingLeads(): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterPendingLeads()
    }

    fun hitGetServiceCenterAssignedLeads(): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterAssignedLeads()
    }

    fun hitGetServiceCenterCancelledLeads(): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCancelledLeads()
    }

    fun hitServiceCenterCancelLeads(leadId: Int): Call<CancelLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCancelLead(leadId)
    }
}