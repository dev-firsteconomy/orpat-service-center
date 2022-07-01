package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import com.orpatservice.app.data.model.PaginationData

class TakCompletedResponse (

    var success: Boolean,
    var data    : TaskCompletedListData,
    var message: String
)

data class TaskCompletedListData (
    val data: ArrayList<TaskCompletedRequestData> = arrayListOf(),
    val pagination : PaginationData
)