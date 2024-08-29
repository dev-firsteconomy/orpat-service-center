package com.orpatservice.app.ui.leads.customer_detail

data class UploadFileResponse(
    val data: UploadFileData,
    val message: String,
    val success: Boolean
)

data class UploadFileData(
    val invoice_url: String
)