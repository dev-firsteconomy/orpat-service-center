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
    var modelNo: String? = null
    var invoiceImage: String? = null
    var qrImage: String? = null
    var purchaseAt: String? = null
    var serviceCenterAssignedAt: String? = null
    var technicianAssignedAt: String? = null
    var createdAt: String? = null
    var natureOfComplain: String? = null
    var technician: String? = null
}