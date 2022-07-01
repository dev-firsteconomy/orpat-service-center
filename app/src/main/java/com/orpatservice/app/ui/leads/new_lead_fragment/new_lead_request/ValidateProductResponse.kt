package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

data class ValidateQrcodeResponse (
    val message: String,
    val data: PaidLead,
    val success: Boolean
)
data class PaidLead (
    val request_id: Int,
    val service_distributor_name: String,
    val mobile: String
)