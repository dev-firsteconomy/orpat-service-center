package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

class UpdatePartsRequestData (
    val success: Boolean,
   // val data: UpdatedPartsData,
    val message: String
    )
data class UpdatedPartsData(
    val id: String,
    val name: String,
    val email: String,

    )
