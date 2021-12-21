package com.orpatservice.app.ui.addtechnician

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.repository.DataRepository
import okhttp3.MultipartBody

class TechniciansViewModel : ViewModel() {

    fun loadTechnician(): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.loadTechnician()
    }

    fun hitAPIAddTechnician(
        requestBody: MultipartBody
    ): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIAddTechnician(requestBody)
    }

    fun hitAPIUpdateTechnician(
        requestBody: MultipartBody,
        technicianID: Int?
    ): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIUpdateTechnician(requestBody,technicianID)
    }

}