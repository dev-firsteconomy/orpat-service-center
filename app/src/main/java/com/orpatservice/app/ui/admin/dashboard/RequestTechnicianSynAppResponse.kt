package com.orpatservice.app.ui.admin.dashboard

class RequestTechnicianSynAppResponse (
    var success: Boolean,
    var data    : NotificationBadges,
    var message: String
    )

    data class NotificationBadges (
        val notification_badge_count: BadgeCounts
    )


    data class BadgeCounts (
        val barcode_request_tab: String,
      //  val no_barcode_request_tab: String
    )

