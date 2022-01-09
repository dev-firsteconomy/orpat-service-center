package com.orpatservice.app.utils

object Constants {

    const val MODULE_TYPE = "MODULE_TYPE"
    const val LEAD_DATA= "LEAD_DATA"

    //Constants for login session
    const val TOKEN = "TOKEN"
    const val NO_TOKEN = ""
    const val SERVICE_CENTER_DATA = "SERVICE_CENTER"

    const val MOBILE_NUMBER = "MOBILE_NUMBER"
    const val USER_TYPE = "USER_TYPE"
    const val SERVICE_CENTER = "SERVICE_CENTER"
    const val TECHNICIAN = "TECHNICIAN"

    const val REQUEST = "Requests"
    val requestsTabNameArray = arrayOf(
        "New Request",
        "Assign To Technician"
    )

    const val HISTORY = "History"
    val requestsHistoryTabNameArray = arrayOf(
        "Completed Request",
        "Cancelled Request"
    )

    //Fragment Type
    const val LEAD_TYPE = "LEAD_TYPE"
    const val LEAD_NEW = "LEAD_NEW"
    const val LEAD_ASSIGN_TECHNICIAN = "LEAD_ASSIGN_TECHNICIAN"
    const val LEAD_COMPLETED_REQUEST = "LEAD_COMPLETED_REQUEST"
    const val LEAD_CANCELLED_REQUEST = "LEAD_CANCELLED_REQUEST"

    const val AVAILABLE = "Available"
    const val NOT_AVILABLE = "Not available"

    //Leads status
    const val PENDING = "Pending"
    const val TECHNICIAN_ASSIGNED = "Technician Assined"

    // Technician
    const val IS_NAV = "IS_NAV"
    const val LEADS_ID = "LEADS_ID"
    const val IMAGE_URL = "IMAGE_URL"
    const val SELECTED_PARTS = "SELECTED_PARTS"

    object ComingFrom {

        val CUSTOMER_DETAILS = "CUSTOMER_DETAILS"
        val DASHBOARD = "DASHBOARD"

    }

}