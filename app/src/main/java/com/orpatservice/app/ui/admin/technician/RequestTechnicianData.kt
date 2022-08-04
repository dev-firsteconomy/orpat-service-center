package com.orpatservice.app.ui.admin.technician

data class RequestTechnicianData (

    var success: Boolean,
    var data    : Technician,
    var message: String
)

data class Technician (
    val data: ArrayList<TechnicianList> = arrayListOf(),
)
