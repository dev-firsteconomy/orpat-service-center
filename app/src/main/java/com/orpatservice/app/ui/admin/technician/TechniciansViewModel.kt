package com.orpatservice.app.ui.admin.technician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.SaveEnquiryResponse
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.repository.DataRepository
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.UpdatePartsRequestData
import com.orpatservice.app.utils.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Vikas Singh on 16/12/21.
 */
class TechniciansViewModel : ViewModel() {
    val assignTechnicianData = MutableLiveData<Resource<RequestLeadResponse>>()
    val assignToTechnicianList = MutableLiveData<Resource<NewRequestResponse>>()
    val changeTechnicianList = MutableLiveData<Resource<NewRequestResponse>>()
    val completedTechnicianData = MutableLiveData<Resource<RequestLeadResponse>>()

    fun loadTechnician(nextPage : Int): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.loadTechnician(nextPage)
    }

    fun hitAPIAddTechnician(
        requestBody: MultipartBody
    ): LiveData<Resource<AddTechnicianResponse>> {
        return DataRepository.instance.hitAPIAddTechnician(requestBody)
    }

   /* fun loadAssignedLeads(pageNumber: Int) {
        DataRepository.instance.hitGetServiceCenterAssignedLeads(pageNumber)
            .enqueue(callbackAssignedLeads)
    }*/

    fun hitAPIUpdateTechnician(
        requestBody: MultipartBody,
        technicianID: Int?
    ): LiveData<Resource<AddTechnicianResponse>> {
        return DataRepository.instance.hitAPIUpdateTechnician(requestBody,technicianID)
    }

    fun searchAssignedLeads(keyword: String) {
       // if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitServiceCenterSearchAssignedLeads(keyword).enqueue(callbackAssignedLeads)
       // }
    }

    fun searchCompletedLeads(keyword: String) {
        // if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
        DataRepository.instance.hitAPICompletedLeads(keyword).enqueue(callbackCompletedLeads)
        // }
    }

    fun hitAPIAssignTechnician(leadsId: Int?, technicianId: Int): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIAssignTechnician(leadsId,technicianId)
    }

    fun hitAPIAssignTechnicianLead(leadsId: String?, technicianId: String): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIAssignTechnicianLead(leadsId,technicianId)
    }

    fun hitAPIAssignChangeTechnicianLead(leadsId: String?, technicianId: String): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIAssignChangeTechnicianLead(leadsId,technicianId)
    }


    fun hitAPIParts(search : String): LiveData<Resource<RepairPartResponse>> {
        return DataRepository.instance.hitAPIParts(search)
    }

    fun hitAPIRepairPartTechnician(
        requestBody: MultipartBody,
        complaint_id : String,
        technician_id : String
    ): LiveData<Resource<SaveEnquiryResponse>> {
        return DataRepository.instance.hitAPIRepairPartTechnician(requestBody,complaint_id,technician_id)
    }

    fun hitAPISendHappyCode(leadId : String): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPISendHappyCode(leadId)
    }
    fun hitAPITaskSendHappyCode(leadId : String): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPITaskSendHappyCode(leadId)
    }

    fun hitAPITaskSendHappyCodeVerification(leadId : String,happyCode : String): LiveData<Resource<UpdatePartsRequestData>> {
        return DataRepository.instance.hitAPITaskSendHappyCodeVerification(leadId,happyCode)
    }

    fun hitAPIMarkAsComplete(leadId : String,remark : String,verificationCode : String): LiveData<Resource<SaveEnquiryResponse>> {
        return DataRepository.instance.hitAPIMarkAsComplete(leadId,remark,verificationCode)
    }
   /* fun assignTechnicianLead() {
        DataRepository.instance.hitGetAssignTechnicianLeads().enqueue(callbackAssignTechnicianLead)
    }*/

    fun loadAssignedTechnicianLeads(pageNumber: Int) {
        DataRepository.instance.hitGetServiceCenterAssignedTechnicianLeads(pageNumber)
            .enqueue(callbackAssignTechnicianLead)
    }

    fun assignTechnicianLead(pageNumber: Int) {
        DataRepository.instance.hitGetServiceCenterAssignedLeads(pageNumber)
            .enqueue(callbackAssignedLeads)
    }

    fun completedTechnicianLead(pageNumber: Int) {
        DataRepository.instance.hitGetServiceCenterCompletedDataLeads(pageNumber)
            .enqueue(callbackCompletedLeads)
    }


    fun changeTechnicianLead(pageNumber: Int) {
        DataRepository.instance.hitGetServiceCenterChangeAssignedLeads(pageNumber)
            .enqueue(callbackChangeAssignedLeads)
    }


    private val callbackAssignTechnicianLead: Callback<NewRequestResponse> = object :
        Callback<NewRequestResponse> {
        override fun onResponse(
            call: Call<NewRequestResponse>,
            response: Response<NewRequestResponse>
        ) {
            if (response.isSuccessful) {
                assignToTechnicianList.value = response.body()?.let {
                    Resource.success(it) }
                //  getUserData()
            } else {
                assignToTechnicianList.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<NewRequestResponse>, t: Throwable) {
            assignToTechnicianList.value = Resource.error(ErrorUtils.getError(t))
        }
    }

    private val callbackAssignedLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    assignTechnicianData.postValue(response.body().let { Resource.success(it) } )
                } else {
                    assignTechnicianData.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                assignTechnicianData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }


    private val callbackCompletedLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    completedTechnicianData.postValue(response.body().let { Resource.success(it) } )
                } else {
                    completedTechnicianData.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                completedTechnicianData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }

    private val callbackChangeAssignedLeads: Callback<NewRequestResponse> =
        object : Callback<NewRequestResponse> {
            override fun onResponse(
                call: Call<NewRequestResponse>,
                response: Response<NewRequestResponse>) {

                if (response.isSuccessful) {
                    changeTechnicianList.postValue(response.body().let { Resource.success(it) } )
                } else {
                    changeTechnicianList.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<NewRequestResponse>, t: Throwable) {
                changeTechnicianList.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }
}