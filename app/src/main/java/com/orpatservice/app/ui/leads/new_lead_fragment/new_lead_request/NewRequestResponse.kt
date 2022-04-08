package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import com.orpatservice.app.data.model.PaginationData

class NewRequestResponse (

    var success: Boolean,
    var data    : ListData,
    var message: String
    )

    data class ListData (
        val data: ArrayList<RequestData> = arrayListOf(),
        val pagination : PaginationData
    )