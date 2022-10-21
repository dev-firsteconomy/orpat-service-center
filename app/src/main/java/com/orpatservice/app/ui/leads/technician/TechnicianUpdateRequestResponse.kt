package com.orpatservice.app.ui.leads.technician

import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage

data class TechnicianUpdateRequestResponse (
    val message: String,
    val data: UpdateData,
    val success: Boolean
    )
    data class UpdateData(
        /*val technician_detail_status: String,
        val pending_lead_enqury_detail_count: String,
        val in_warranty_enquiries_count: String,
        val pending_technician_detail: String,*/
        var invoice_url: String?,
        var scanned_barcode: String? ,
        // var qr_image: String?,
        var purchase_at: String? ,
        var complaint_preset: String? ,
        var in_warranty: String?,
        var status: Boolean = false,
        var customer_discription: String?,
        var service_center_discription: String?,
        var dummy_barcode: String? ,
        var model_no: String? ,
        var technician_detail_status: String? ,
        var technician_scan_status: String? ,
        val pending_lead_enqury_detail_count: String,
        val in_warranty_enquiries_count: String,
        val pending_technician_detail: String,
        var warranty_parts: ArrayList<WarrantryPart> = arrayListOf(),
        var lead_enquiry_images: ArrayList<TechnicianEnquiryImage> = arrayListOf()


    )