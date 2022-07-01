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
import com.orpatservice.app.data.remote.ApiClient
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.leads.customer_detail.CancelRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UpdateRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TakCompletedResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.UpdatePartsRequestData
import com.orpatservice.app.ui.leads.technician.TechnicianUpdateRequestResponse
import com.orpatservice.app.ui.leads.technician.ValidateProductResponse
import com.orpatservice.app.ui.leads.technician.response.TechnicianRequestLeadResponse
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


    fun hitServiceValidateProductApi(scanned_barcode: String): Call<ValidateProductResponse> {
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getClient().validateProduct(token,scanned_barcode)
    }


    fun hitCustomerValidateProductApi(
        requestBody: JsonObject,
        technicianId: String? ,

    ): LiveData<Resource<UpdateRequestResponse>> {
        val mutableLiveData = MutableLiveData<Resource<UpdateRequestResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().validateProduct(token,technicianId,requestBody)
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


    fun loadTechnician(nextPage : Int): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().getTechnicianAPI(token,nextPage)
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
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitAPIAddTechnician(token,requestBody)
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



   /* fun hitTaskUpdateRequestApi(
        requestBody: JsonObject,
        leadId: Int? ,
    ): LiveData<Resource<UpdateRequestResponse>> {
        val mutableLiveData = MutableLiveData<Resource<UpdateRequestResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitTaskUpdateRequestLead(leadId,token,requestBody)
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
    }*/

    fun hitTaskUpdateRequestApi(data: JsonObject,leadId: Int?): Call<UpdateRequestResponse> {
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getClient().hitTaskUpdateRequestLead(token,data,leadId)
    }

    fun hitAPIUploadPartsComplete(leadsId: Int,happyCode: String?): Call<UpdatePartsRequestData> {
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getClient().hitAPIServiceMarkAsComplete(token,leadsId.toString(),happyCode)
    }



    fun hitTaskCompletedRequestApi(
        leadId: Int? ,
    ): LiveData<Resource<TechnicianRequestLeadResponse>> {
        val mutableLiveData = MutableLiveData<Resource<TechnicianRequestLeadResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitTaskCompletedRequestLead(leadId,token)
            .enqueue(object : Callback<TechnicianRequestLeadResponse> {

                override fun onFailure(call: Call<TechnicianRequestLeadResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<TechnicianRequestLeadResponse>,
                    response: Response<TechnicianRequestLeadResponse>
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
    fun hitTechnicianUpdateRequestApi(
        requestBody: JsonObject,
        taskId : Int?,
    ): LiveData<Resource<TechnicianUpdateRequestResponse>> {
        val mutableLiveData = MutableLiveData<Resource<TechnicianUpdateRequestResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitTechnicianUpdateRequestLead(token,taskId,requestBody)
            .enqueue(object : Callback<TechnicianUpdateRequestResponse> {

                override fun onFailure(call: Call<TechnicianUpdateRequestResponse>, t: Throwable) {
                    mutableLiveData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<TechnicianUpdateRequestResponse>,
                    response: Response<TechnicianUpdateRequestResponse>
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


    fun hitAPIAssignTechnicianLead(leadsId: String?, technicianId: String): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitAPIAssignTechnicianLead(token,leadsId,technicianId)
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

    fun hitAPIAssignChangeTechnicianLead(leadsId: String?, technicianId: String): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitAPIAssignChangeTechnicianLead(token,leadsId,technicianId)
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

    //Lead API assign to tech
    fun hitAPIAssignTechnician(leadsId: Int?, technicianId: Int): LiveData<Resource<TechnicianResponse>> {
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
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
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

    fun hitAPITaskSendHappyCode(leadId : String): LiveData<Resource<TechnicianResponse>> {
        val mutableTestData = MutableLiveData<Resource<TechnicianResponse>>()

        mutableTestData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitAPITaskSendHappyCode(leadId,token)
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

    fun hitAPITaskSendHappyCodeVerification(leadId : String,happyCode : String): LiveData<Resource<UpdatePartsRequestData>> {
        val mutableTestData = MutableLiveData<Resource<UpdatePartsRequestData>>()

        mutableTestData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
         ApiClient.getAuthApi().hitAPITaskSendHappyCodeVerification(leadId,token,happyCode)
            .enqueue(object : Callback<UpdatePartsRequestData> {

                override fun onFailure(call: Call<UpdatePartsRequestData>, t: Throwable) {
                    mutableTestData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<UpdatePartsRequestData>,
                    response: Response<UpdatePartsRequestData>
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

    /*fun hitAPIServiceMarkAsComplete(leadId : String, happyCode:String): LiveData<Resource<RequestLeadResponse>> {
        val mutableTestData = MutableLiveData<Resource<RequestLeadResponse>>()

        mutableTestData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        ApiClient.getAuthApi().hitAPIServiceMarkAsComplete(leadId,happyCode,token)
            .enqueue(object : Callback<RequestLeadResponse> {

                override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                    mutableTestData.value = Resource.error(ErrorUtils.getError(t))
                }

                override fun onResponse(
                    call: Call<RequestLeadResponse>,
                    response: Response<RequestLeadResponse>
                ) {
                    if (response.isSuccessful) {
                        println("response.body()?.data"+response.body()?.data)
                        mutableTestData.value = response.body()?.let {
                            Resource.success(it)
                        }

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
    }*/


    fun hitAPIUploadFile(
        requestBody: MultipartBody,
    ): Call<UploadFileResponse> {
        val mutableLiveData = MutableLiveData<Resource<UploadFileResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().hitAPIUploadFile(token,requestBody)

    }

    fun hitAPITechnicianUploadFile(
        requestBody: MultipartBody,
    ): Call<UploadFileResponse> {
        val mutableLiveData = MutableLiveData<Resource<UploadFileResponse>>()

        mutableLiveData.value = (Resource.loading(null))
        val token :String = "Bearer "+SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().hitAPITechnicianUploadFile(token,requestBody)

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
        println("tokentoken"+token)
        return ApiClient.getAuthApi().getServiceCenterPendingLeads(token,pageNumber)
    }

    fun hitServiceCenterSearchPendingLeads(keyword: String): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterSearchPendingLeads(keyword,token)
    }

    fun hitGetServiceCenterAssignedLeads(pageNumber: Int): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterAssignedLeads(token,pageNumber)
    }

    fun hitGetServiceCenterChangeAssignedLeads(pageNumber: Int): Call<NewRequestResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterAssignedTechnicianLeads(token,pageNumber)
    }


    fun hitGetServiceCenterAssignedTechnicianLeads(pageNumber: Int): Call<NewRequestResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterAssignedTechnicianLeads(token,pageNumber)
    }


    fun hitServiceCenterSearchAssignedLeads(keyword: String): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterSearchAssignedLeads(keyword,token)
    }

    fun hitServiceCenterSearchCompletedLeads(keyword: String): Call<TakCompletedResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterSearchCompletedLeads(keyword,token)
    }

    fun hitAPICompletedLeads(keyword: String): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getAPISearchCompletedLeads(keyword,token)
    }

   /* fun hitServiceCenterSearchCompletedLeads(keyword: String): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterSearchAssignedLeads(keyword,token)
    }*/

    fun hitGetServiceCenterCompletedLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCompletedLeads(pageNumber)
    }
    fun hitGetServiceCenterCompletedDataLeads(pageNumber: Int): Call<RequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterCompletedDataLeads(pageNumber,token)
    }


    fun hitGetServiceCenterTaskCompletedLeads(pageNumber: Int): Call<TakCompletedResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getServiceCenterTaskCompletedLeads(token,pageNumber)
    }


    fun hitGetServiceCenterCancelledLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCancelledLeads(pageNumber)
    }

    fun hitServiceCenterCancelLeads(leadId: Int, cancelReason: String): Call<CancelLeadResponse> {
        return ApiClient.getAuthApi().getServiceCenterCancelLead(leadId, cancelReason)
    }

    fun hitGetTechnicianPendingLeads(pageNumber: Int): Call<TechnicianRequestLeadResponse> {
        val token :String = "Bearer "+ SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
        return ApiClient.getAuthApi().getTechnicianPendingLeads(token,pageNumber)
    }

    fun hitTechnicianSearchPendingLeads(keyword: String): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getTechnicianSearchPendingLeads(keyword)
    }

    fun hitGetTechnicianCompletedLeads(pageNumber: Int): Call<RequestLeadResponse> {
        return ApiClient.getAuthApi().getTechnicianCompletedLeads(pageNumber)
    }
}