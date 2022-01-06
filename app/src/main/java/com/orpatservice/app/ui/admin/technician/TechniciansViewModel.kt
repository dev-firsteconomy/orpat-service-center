package com.orpatservice.app.ui.admin.technician

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.AddTechnicianResponse
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

}