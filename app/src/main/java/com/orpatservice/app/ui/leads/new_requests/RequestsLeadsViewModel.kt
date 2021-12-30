package com.orpatservice.app.ui.leads.new_requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.data.remote.ErrorUtils
import com.orpatservice.app.ui.data.repository.DataRepository
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ajay Yadav on 21/12/21.
 */
class RequestsLeadsViewModel : ViewModel() {

    val pendingLeadsData = MutableLiveData<Resource<RequestLeadResponse>>()
    val assignedLeadsData = MutableLiveData<Resource<RequestLeadResponse>>()
    val cancelledLeadsData = MutableLiveData<Resource<RequestLeadResponse>>()

    fun loadPendingLeads() {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitGetServiceCenterPendingLeads().enqueue(callbackPendingLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            //In-Progress APIs
        }
    }

    private val callbackPendingLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    pendingLeadsData.value = response.body().let { Resource.success(it) }
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
        }

    fun loadAssignedLeads() {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitGetServiceCenterAssignedLeads()
                .enqueue(callbackAssignedLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)){
            //In-progress APIs
        }
    }

    private val callbackAssignedLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    assignedLeadsData.value = response.body().let { Resource.success(it) }
                } else {
                    assignedLeadsData.value =
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        )
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                assignedLeadsData.value = Resource.error(ErrorUtils.getError(t))
            }
        }

    fun loadCancelledLeads() {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitGetServiceCenterCancelledLeads()
                .enqueue(callbackCancelledLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)){
            //In-progress APIs
        }
    }

    private val callbackCancelledLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    cancelledLeadsData.value = response.body().let { Resource.success(it) }
                } else {
                    cancelledLeadsData.value =
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        )
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                cancelledLeadsData.value = Resource.error(ErrorUtils.getError(t))
            }
        }
}