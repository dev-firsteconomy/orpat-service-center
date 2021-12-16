package com.orpatservice.app.ui.addtechnician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.repository.DataRepository

class TechniciansViewModel : ViewModel() {

    fun loadTechnician(): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.loadTechnician()
    }

    fun hitAPIAddTechnician(): LiveData<Resource<TechnicianResponse>> {
        return DataRepository.instance.hitAPIAddTechnician()
    }

}