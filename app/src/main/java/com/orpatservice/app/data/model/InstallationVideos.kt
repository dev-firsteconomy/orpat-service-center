package com.orpatservice.app.data.model

import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskCompletedRequestData

data class VideoProductCategoriesData (
    val id : String,
    val name : String
)

data class TaskCompletedListData (
    val data: ArrayList<TaskCompletedRequestData> = arrayListOf(),
    val pagination : PaginationData
)