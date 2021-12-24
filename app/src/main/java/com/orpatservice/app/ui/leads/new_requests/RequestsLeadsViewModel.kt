package com.orpatservice.app.ui.leads.new_requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.data.remote.ErrorUtils
import com.orpatservice.app.ui.data.repository.DataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ajay Yadav on 21/12/21.
 */
class RequestsLeadsViewModel: ViewModel() {

    val pendingLeadsData = MutableLiveData<Resource<RequestLeadResponse>>()

    fun loadPendingLeads() {
        DataRepository.instance.hitGetPendingLeads()
            .enqueue(object : Callback<RequestLeadResponse> {
                override fun onResponse(
                    call: Call<RequestLeadResponse>,
                    response: Response<RequestLeadResponse>
                ) {
                    if (response.isSuccessful) {
                        pendingLeadsData.value = response.body()?.let { Resource.success(it) }
                    } else {
                        pendingLeadsData.value =
                            Resource.error(
                                ErrorUtils.getError(
                                    response.errorBody(),
                                    response.code()
                                )
                            )
                    }
                }

                override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                    pendingLeadsData.value = Resource.error(ErrorUtils.getError(t))
                }
            })
    }
}