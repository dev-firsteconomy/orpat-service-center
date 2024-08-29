package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.TaskUpdateAdapter

data class LeadList(
    //val viewType: Int, val taskEnquiry: ArrayList<TaskEnquiry>, val warranty : String
    val warrantyParts: String,
    val enquiry: Enquiry
)