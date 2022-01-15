package com.orpatservice.app.ui.admin.technician

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.repository.DataRepository
import okhttp3.MultipartBody

/**
 * Created by Vikas Singh on 16/12/21.
 */
class TechniciansViewModel : ViewModel() {

    fun loadTechnician(nextPage : Int): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.loadTechnician(nextPage)
    }

    fun hitAPIAddTechnician(
        requestBody: MultipartBody
    ): LiveData<Resource<AddTechnicianResponse>> {
        return DataRepository.instance.hitAPIAddTechnician(requestBody)
    }

    fun hitAPIUpdateTechnician(
        requestBody: MultipartBody,
        technicianID: Int?
    ): LiveData<Resource<AddTechnicianResponse>> {
        return DataRepository.instance.hitAPIUpdateTechnician(requestBody,technicianID)
    }


    fun hitAPIAssignTechnician(leadsId : Int,technicianId : Int): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIAssignTechnician(leadsId,technicianId)
    }

    fun hitAPIParts(search : String): LiveData<Resource<RepairPartResponse>> {
        return DataRepository.instance.hitAPIParts(search)
    }

    fun hitAPIRepairPartTechnician(
        requestBody: MultipartBody,
        complaint_id : String,
        technician_id : String
    ): LiveData<Resource<AddTechnicianResponse>> {
        return DataRepository.instance.hitAPIRepairPartTechnician(requestBody,complaint_id,technician_id)
    }

    fun hitAPISendHappyCode(leadId : String): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPISendHappyCode(leadId)
    }

}