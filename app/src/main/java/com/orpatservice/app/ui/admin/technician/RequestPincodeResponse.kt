package com.orpatservice.app.ui.admin.technician

import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.admin.dashboard.BadgeCount

class RequestPincodeResponse (

    var success: Boolean,
    var data    : Pincode,
    var message: String
)

data class Pincode (
    val pincodes: ArrayList<PincodeData> = arrayListOf(),
)
