package com.orpatservice.app.ui.leads.service_center.response

data class OrderCountResponse (
    val message: String,
    val data: Data,
    val success: Boolean
)

data class Data(
    var app_order_count: OrderCountData?,
)
data class OrderCountData(

    var new_request_count: Int?,
    var assigned_request_count: Int?,
    var verify_request_count: Int?,
    var history_completed_request_count: Int?,
    var history_cancelled_request_count: Int?,
    var chargeable_new_request_count: Int?,
    var chargeable_completed_request_count: Int?,
    var chargeable_cancelled_request_count: Int?,

)