package com.orpatservice.app.ui.leads.customer_detail

import com.orpatservice.app.data.model.requests_leads.Enquiry

data class UpdateRequestResponse (
    val message: String,
    val data: Enquiry,
    val success: Boolean
)
data class UpdateData(


    val detail_status: String,
    val pending_lead_enqury_detail_count: String,
    val in_warranty_enquiries_count: String
)