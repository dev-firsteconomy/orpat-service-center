package com.orpatservice.app.utils

object Constants {

    const val MODULE = "MODULE"
    const val LEAD_DATA= "LEAD_DATA"

    //Constants for login session
    const val TOKEN = "TOKEN"
    const val NO_TOKEN = ""
    const val SERVICE_CENTER = "SERVICE_CENTER"

    const val MOBILE_NUMBER = "MOBILE_NUMBER"
    const val USER_TYPE = "USER_TYPE"
    const val ADMIN = "ADMIN"
    const val TECHNICIAN = "TECHNICIAN"

    const val REQUEST = "REQUEST"
    val requestsTabNameArray = arrayOf(
        "New Request",
        "Assign To Technician"
    )

    const val HISTORY = "HISTORY"
    val requestsHistoryTabNameArray = arrayOf(
        "Completed Request",
        "Cancelled Request"
    )

    //Fragment Type
    const val NEW_LEAD = "NEW_LEAD"
    const val ASSIGN_TECHNICIAN = "ASSIGN_TECHNICIAN"
    const val COMPLETED_REQUEST = "COMPLETED_REQUEST"
    const val CANCELLED_REQUEST = "CANCELLED_REQUEST"
}