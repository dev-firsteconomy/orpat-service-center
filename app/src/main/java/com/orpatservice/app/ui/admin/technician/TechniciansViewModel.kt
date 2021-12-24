package com.orpatservice.app.ui.admin.technician

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.AddTechnicianResponse
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.repository.DataRepository
import okhttp3.MultipartBody

class TechniciansViewModel : ViewModel() {

    fun loadTechnician(): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.loadTechnician()
    }
    fun loadNextTechnician(nextPage : Int): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.loadNextPageTechnician(nextPage)
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

}