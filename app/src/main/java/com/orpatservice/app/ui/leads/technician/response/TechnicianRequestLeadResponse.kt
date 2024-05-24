package com.orpatservice.app.ui.leads.technician.response

import com.orpatservice.app.data.model.PaginationData
import com.orpatservice.app.data.model.requests_leads.LeadData

class TechnicianRequestLeadResponse (

    var success: Boolean,
    var data    : ListData,
    var message: String,
    var code: Int
    )

data class ListData (
    val data: ArrayList<TechnicianLeadData> = arrayListOf(),
    val pagination : PaginationData
)

