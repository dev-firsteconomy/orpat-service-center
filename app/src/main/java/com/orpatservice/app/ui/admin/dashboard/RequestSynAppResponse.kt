package com.orpatservice.app.ui.admin.dashboard


 class RequestSynAppResponse(
    var success: Boolean,
    var data    : NotificationBadge,
    var message: String,
    var code: Int
)

data class NotificationBadge (
    val notification_badge_count: BadgeCount
)


data class BadgeCount (
    val barcode_request_tab: String,
    val no_barcode_request_tab: String
)
