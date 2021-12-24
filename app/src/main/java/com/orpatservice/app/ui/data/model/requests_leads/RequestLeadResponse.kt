package com.orpatservice.app.ui.data.model.requests_leads

/**
 * Created by Ajay Yadav on 24/12/21.
 */
class RequestLeadResponse(
    var success: Boolean? = null,
    var data: ArrayList<LeadData> = arrayListOf(),
    var message: String? = null
)
