package com.orpatservice.app.ui.leads.new_requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.model.requests_leads.CancelLeadResponse
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
    val cancelLeadsData = MutableLiveData<Resource<CancelLeadResponse>>()
    val completedLeadsData = MutableLiveData<Resource<RequestLeadResponse>>()

    fun loadPendingLeads(pageNumber: Int) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitGetServiceCenterPendingLeads(pageNumber).enqueue(callbackPendingLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            DataRepository.instance.hitGetTechnicianPendingLeads(pageNumber).enqueue(callbackPendingLeads)
        }
    }

    fun searchPendingLeads(keyword: String) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitServiceCenterSearchPendingLeads(keyword).enqueue(callbackPendingLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            DataRepository.instance.hitTechnicianSearchPendingLeads(keyword).enqueue(callbackPendingLeads)
        }
    }

    private val callbackPendingLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    pendingLeadsData.postValue(response.body().let { Resource.success(it) }) // = response.body().let { Resource.success(it) }
                } else {
                    pendingLeadsData.postValue(Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    ))
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                pendingLeadsData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }

    fun loadAssignedLeads(pageNumber: Int) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitGetServiceCenterAssignedLeads(pageNumber)
                .enqueue(callbackAssignedLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)){
            //In-progress APIs
        }
    }

    fun searchAssignedLeads(keyword: String) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitServiceCenterSearchAssignedLeads(keyword).enqueue(callbackAssignedLeads)
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            //In-Progress APIs
        }
    }

    private val callbackAssignedLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    assignedLeadsData.postValue(response.body().let { Resource.success(it) } )
                } else {
                    assignedLeadsData.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                assignedLeadsData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }

    fun loadCancelledLeads(pageNumber: Int) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            DataRepository.instance.hitGetServiceCenterCancelledLeads(pageNumber)
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
                    cancelledLeadsData.postValue(response.body().let { Resource.success(it) })
                } else {
                    cancelledLeadsData.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                cancelledLeadsData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }

    fun doCancelLead(leadId: Int)
    {
        DataRepository.instance.hitServiceCenterCancelLeads(leadId)
            .enqueue(callbackCancelLeads)
    }

    private val callbackCancelLeads: Callback<CancelLeadResponse> =
        object : Callback<CancelLeadResponse> {
            override fun onResponse(
                call: Call<CancelLeadResponse>,
                response: Response<CancelLeadResponse>) {

                if (response.isSuccessful) {
                    cancelLeadsData.postValue(response.body().let { Resource.success(it) })
                } else {
                    cancelLeadsData.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<CancelLeadResponse>, t: Throwable) {
                cancelLeadsData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }

    fun loadCompletedLeads(pageNumber: Int) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {

        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)){
            DataRepository.instance.hitGetTechnicianCompletedLeads(pageNumber)
                .enqueue(callbackCompletedLeads)
        }
    }

    private val callbackCompletedLeads: Callback<RequestLeadResponse> =
        object : Callback<RequestLeadResponse> {
            override fun onResponse(
                call: Call<RequestLeadResponse>,
                response: Response<RequestLeadResponse>) {

                if (response.isSuccessful) {
                    completedLeadsData.postValue(response.body().let { Resource.success(it) })
                } else {
                    completedLeadsData.postValue(
                        Resource.error(
                            ErrorUtils.getError(
                                response.errorBody(),
                                response.code()
                            )
                        ))
                }
            }

            override fun onFailure(call: Call<RequestLeadResponse>, t: Throwable) {
                completedLeadsData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }
}