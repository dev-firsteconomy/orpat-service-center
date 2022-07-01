package com.orpatservice.app.ui.leads.technician

data class TechnicianUpdateRequestResponse (
    val message: String,
    val data: UpdateData,
    val success: Boolean
    )
    data class UpdateData(
        val technician_detail_status: String,
        val pending_lead_enqury_detail_count: String,
        val in_warranty_enquiries_count: String,
        val pending_technician_detail: String,

    )