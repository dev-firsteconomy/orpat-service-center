package com.orpatservice.app.ui.data.model.requests_leads

import com.orpatservice.app.ui.data.model.PaginationData

/**
 * Created by Ajay Yadav on 24/12/21.
 */
class RequestLeadResponse(
    var success: Boolean,
    var data    : ListData,
    var message: String
)

data class ListData (
    val leadData: ArrayList<LeadData> = arrayListOf(),
    val pagination : PaginationData
)