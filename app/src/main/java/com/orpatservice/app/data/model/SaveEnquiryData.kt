package com.orpatservice.app.data.model

class SaveEnquiryData(
    val id: Int,
    val model_no: String,
    val invoice_image: String,
    val qr_image: String,
    val purchase_at: String,
    val nature_pf_complain: String,
    val in_warranty: String,
    val replacement_part_id: String,
    val replacement_image: String,
    val part: String,
    val status: Boolean
)