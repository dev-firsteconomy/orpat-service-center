package com.orpatservice.app.ui.leads.customer_detail

data class UpdateRequestResponse (
    val message: String,
    val data: UpdateData,
    val success: Boolean
)
data class UpdateData(
    val detail_status: String
)