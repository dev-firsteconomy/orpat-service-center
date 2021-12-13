package com.orpatservice.app.ui.data.model

data class TechnicianResponse(val success: Boolean, val data: TechnicianBaseData, val message: String)

data class TechnicianBaseData(val data: ArrayList<TechnicianData>)
