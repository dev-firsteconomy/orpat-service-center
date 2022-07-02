package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

class VerifyGSTRequestData (
    val success: Boolean,
     val data: VerifyData,
    val message: String
)
data class VerifyData(
    val gstin: String,
    val firm_name: String,
    val trade_name: String,
    val status: String,

    )
