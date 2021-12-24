package com.orpatservice.app.ui.data.model.requests_leads

import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class LeadData: Serializable {
    var id: Int? = null
    var name: String? = null
    var email: String? = null
    var mobile: String? = null
    var pincode: String? = null
    var address: String? = null
    var status: String? = null
    var city: String? = null
    var state: String? = null
    var model_no: String? = null
    var invoice_image: String? = null
    var qr_image: String? = null
    var purchase_at: String? = null
    var service_center_assigned_at: String? = null
    var technician_assigned_at: String? = null
    var created_at: String? = null
    var nature_of_complain: String? = null
    var technician: String? = null
}