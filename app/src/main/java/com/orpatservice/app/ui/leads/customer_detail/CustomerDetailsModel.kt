package com.orpatservice.app.ui.leads.customer_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.SaveEnquiryResponse
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.repository.DataRepository
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerDetailsModel: ViewModel() {
    val invoiceUploadData = MutableLiveData<Resource<UploadFileResponse>>()
    val invoiceUpdateRequestData = MutableLiveData<Resource<UpdateRequestResponse>>()
    val assignTechnicianData = MutableLiveData<Resource<RequestLeadResponse>>()

    fun hitUploadFile(requestBody: MultipartBody) {
        return DataRepository.instance.hitAPIUploadFile(requestBody).enqueue(callbackUploadFile)
    }

    fun assignTechnicianLead() {
        DataRepository.instance.hitGetAssignTechnicianLeads().enqueue(callbackAssignTechnicianLead)
    }

    /*fun hitUpdateRequest(requestBody: JsonObject,leadId: String, taskId: String) {
       // return DataRepository.instance.hitUpdateRequestApi(requestBody).enqueue(callbackUploadFile)
        return DataRepository.instance.hitUpdateRequestApi(requestBody,leadId,taskId).enqueue(callbackUpdateRequest)
    }*/
    fun hitUpdateRequest(
        requestBody: JsonObject,
        leadId: Int?,
        taskId: Int?,
    ): LiveData<Resource<UpdateRequestResponse>> {
        return DataRepository.instance.hitUpdateRequestApi(requestBody,leadId,taskId)
    }

    fun hitCancelRequest(
        requestBody: JsonObject,
        leadId : Int?,
    ): LiveData<Resource<CancelRequestResponse>> {
        return DataRepository.instance.hitCancelRequestApi(requestBody,leadId)
    }


    private val callbackUploadFile: Callback<UploadFileResponse> = object :
        Callback<UploadFileResponse> {
        override fun onResponse(
            call: Call<UploadFileResponse>,
            response: Response<UploadFileResponse>
        ) {
            if (response.isSuccessful) {
                invoiceUploadData.value = response.body()?.let { Resource.success(it) }
            } else {
                invoiceUploadData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
            invoiceUploadData.value = Resource.error(ErrorUtils.getError(t))
        }
    }

    private val callbackAssignTechnicianLead: Callback<RequestLeadResponse> = object :
        Callback<RequestLeadResponse> {
        override fun onResponse(
            call: Call<RequestLeadResponse>,
            response: Response<RequestLeadResponse>
        ) {
            if (response.isSuccessful) {
                assignTechnicianData.value = response.body()?.let {
                    Resource.success(it) }
                //  getUserData()
            } else {
                assignTechnicianData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
            assignTechnicianData.value = Resource.error(ErrorUtils.getError(t))
        }
    }
}
