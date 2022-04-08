package com.orpatservice.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.SaveEnquiryResponse
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.login.LoginResponse
import com.orpatservice.app.data.model.login.OTPSendResponse
import com.orpatservice.app.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.data.remote.APIClient
import com.orpatservice.app.data.remote.ApiClient
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.leads.customer_detail.CancelRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UpdateRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.utils.Constants
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

    //Service Center login by email and password
    fun hitServiceCenterLoginApi(email: String, password: String): Call<LoginResponse> {
        return ApiClient.getAuthApi().getServiceCenterLogin(email, password)
    }
    //To get OTP on user register mobile number
    fun hitServiceCenterOTPApi(mobileNumber: String): Call<OTPSendResponse> {
        return ApiClient.getAuthApi().getServiceCenterOtpAPI(mobileNumber)
    }

    //To get OTP on user register mobile number
    fun hitTechnicianOTPApi(mobileNumber: String): Call<OTPSendResponse> {
       // return APIClient.apiAuthInterface().getTechnicianOtpAPI(mobileNumber)
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

    fun loadTechnician(nextPage : Int): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().getTechnicianAPI(nextPage)
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

    fun hitAPIRepairPartTechnician(
        requestBody: MultipartBody,
        complaint_id : String,
        technician_id : String
    ): LiveData<Resource<SaveEnquiryResponse>> {
        val mutableLiveData = MutableLiveData<Resource<SaveEnquiryResponse>>()

        mutableLiveData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPIRepairPartTechnician(requestBody,complaint_id,technician_id)
            .enqueue(object : Callback<SaveEnquiryResponse> {

                override fun onFailure(call: Call<SaveEnquiryResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<SaveEnquiryResponse>,
                    response: Response<SaveEnquiryResponse>
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

    fun hitUpdateRequestApi(
        requestBody: JsonObject,
        leadId: Int? ,
        taskId : Int?,
    ): LiveData<Resource<UpdateRequestResponse>> {
        val mutableLiveData = MutableLiveData<Resource<UpdateRequestResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitUpdateRequestLead(leadId,taskId,token,requestBody)
            .enqueue(object : Callback<UpdateRequestResponse> {

                override fun onFailure(call: Call<UpdateRequestResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<UpdateRequestResponse>,
                    response: Response<UpdateRequestResponse>
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

    fun hitCancelRequestApi(
        requestBody: JsonObject,
        leadId: Int? ,
    ): LiveData<Resource<CancelRequestResponse>> {
        val mutableLiveData = MutableLiveData<Resource<CancelRequestResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitCancelRequestLead(token,requestBody,leadId)
            .enqueue(object : Callback<CancelRequestResponse> {

                override fun onFailure(call: Call<CancelRequestResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<CancelRequestResponse>,
                    response: Response<CancelRequestResponse>
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

    //Lead API assign to tech
    fun hitAPIAssignTechnician(leadsId : Int,technicianId : Int): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPIAssignTechnician(leadsId,technicianId)
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

    fun hitAPIParts(search : String): LiveData<Resource<RepairPartResponse>> {
        val mutableTestData = MutableLiveData<Resource<RepairPartResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPIParts(search)
            .enqueue(object : Callback<RepairPartResponse> {

                override fun onFailure(call: Call<RepairPartResponse>, t: Throwable) {
                    mutableTestData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<RepairPartResponse>,
                    response: Response<RepairPartResponse>
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

    fun hitAPISendHappyCode(leadId : String): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPISendHappyCode(leadId)
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

    fun hitAPIMarkAsComplete(leadId : String,remark : String,verificationCode : String): LiveData<Resource<SaveEnquiryResponse>> {
        val mutableTestData = MutableLiveData<Resource<SaveEnquiryResponse>>()

        mutableTestData.value = (Resource.loading(null))

        ApiClient.getAuthApi().hitAPIMarkAsComplete(leadId,remark,verificationCode)
            .enqueue(object : Callback<SaveEnquiryResponse> {

                override fun onFailure(call: Call<SaveEnquiryResponse>, t: Throwable) {
                    mutableTestData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<SaveEnquiryResponse>,
                    response: Response<SaveEnquiryResponse>
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

    fun hitAPIUploadFile(
        requestBody: MultipartBody,
    ): Call<UploadFileResponse> {
        val mutableLiveData = MutableLiveData<Resource<UploadFileResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().hitAPIUploadFile(token,requestBody)

    }

   /* fun hitUpdateRequestApi(data: JsonObject,leadId: String, taskId: String): Call<UpdateRequestResponse> {
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getClient().updateRequestLead(token,data,leadId,taskId)
    }
*/

    fun hitGetAssignTechnicianLeads(): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getAssignTechnicianLeads(token)
    }


    //Lead API
    fun hitGetServiceCenterPendingLeads(pageNumber: Int): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterPendingLeads(token,pageNumber)
    }

    fun hitServiceCenterSearchPendingLeads(keyword: String): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterSearchPendingLeads(keyword)
    }

    fun hitGetServiceCenterAssignedLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterAssignedLeads(pageNumber)
    }


    fun hitGetServiceCenterAssignedTechnicianLeads(pageNumber: Int): Call<NewRequestResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterAssignedTechnicianLeads(token,pageNumber)
    }


    fun hitServiceCenterSearchAssignedLeads(keyword: String): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterSearchAssignedLeads(keyword)
    }

    fun hitGetServiceCenterCompletedLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCompletedLeads(pageNumber)
    }

    fun hitGetServiceCenterCancelledLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCancelledLeads(pageNumber)
    }

    fun hitServiceCenterCancelLeads(leadId: Int, cancelReason: String): Call<CancelLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCancelLead(leadId, cancelReason)
    }

    fun hitGetTechnicianPendingLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getTechnicianPendingLeads(pageNumber)
    }

    fun hitTechnicianSearchPendingLeads(keyword: String): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getTechnicianSearchPendingLeads(keyword)
    }

    fun hitGetTechnicianCompletedLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getTechnicianCompletedLeads(pageNumber)
    }
}