package com.orpatservice.app.data.model.requests_leads

import com.orpatservice.app.data.model.PaginationData

/**
 * Created by Ajay Yadav on 24/12/21.
 */
class RequestLeadResponse(
    var success: Boolean,
    var data    : ListData,
    var message: String,
    var code: Int
)

data class ListData (
    val data: ArrayList<LeadData> = arrayListOf(),
    val pagination : PaginationData
)