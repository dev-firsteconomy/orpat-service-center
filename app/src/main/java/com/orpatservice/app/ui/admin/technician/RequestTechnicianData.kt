package com.orpatservice.app.ui.admin.technician

import com.orpatservice.app.data.model.PaginationData

data class RequestTechnicianData (

    var success: Boolean,
    var data    : Technician,
    var message: String,
    var code: Int
)

data class Technician (
    val data: ArrayList<TechnicianList> = arrayListOf(),
    val pagination : PaginationData
)
