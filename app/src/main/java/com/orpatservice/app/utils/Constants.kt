package com.orpatservice.app.utils

object Constants {

    const val MODULE_TYPE = "MODULE_TYPE"
    const val LEAD_DATA= "LEAD_DATA"
    const val LEAD_ID= "LEAD_ID"
    const val TASK_DATA= "TASK_DATA"
    const val UPDATE_DATA= "UPDATE_DATA"
    const val IMAGE_DATA= "IMAGE_DATA"
    const val POSITION= "POSITION"
    const val LEAD_DETAILS= "LEAD_DETAILS"
    const val TECHNICIANS_DATA= "TECHNICIANS_DATA"

    const val LATITUDE= "LEAD_DATA"
    const val LONGITUDE= "LEAD_DATA"

    var REQUEST_TOTAL= ""
    var ASSIGN_TOTAL= ""
    var VERIFY_TOTAL= ""
    var COMPLETE_REQ= ""
    var CANCELLED_REQ= ""
    var TECHNICIAN_TOTAL= ""

    var CHARGEABLE_NEW_REQUEST= ""
    var CHARGEABLE_COMPLETED_REQUEST= ""
    var CHARGEABLE_CANCELLED_REQUEST= ""

    //Constants for login session
    const val TOKEN = "TOKEN"
    var REGISTRATION_TOKEN = "REGISTRATION_TOKEN"
    const val NO_TOKEN = ""
    const val SERVICE_CENTER_DATA = "SERVICE_CENTER"

    const val MOBILE_NUMBER = "MOBILE_NUMBER"
    const val USER_TYPE = "USER_TYPE"
    const val SERVICE_CENTER = "SERVICE_CENTER"
    const val TECHNICIAN = "TECHNICIAN"

    const val REQUEST = "Requests"
    /*val requestsTabNameArray = arrayOf(
        "New Request",
        "Assign To Technician",
        "Task Completed"
    )*/

    const val DEVICE_ID = "DEVICE_ID"
    val requestsTabNameArray = arrayOf(
        "New",
        "Assigned",
        "Verify"
    )

   /* val requestsTabNameArray = arrayOf(
        "New",
        "Assigned",
        "Parts",
        "Completed"
    )*/


    val requestsTechnicianTabNameArray = arrayOf(
        "New",
        "Completed Request",
        "Cancelled",
    )


    val orderCountArray = arrayOf(
        "new_request_count",
        "assigned_request_count",
        "verify_request_count",
        "history_completed_request_count",
        "history_cancelled_request_count",
        "chargeable_new_request_count",
        "chargeable_completed_request_count",
        "chargeable_cancelled_request_count",
    )

    const val PARENT = 0
    const val CHILD = 1

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
    var IMAGE_URL = "IMAGE_URL"
    const val SELECTED_PARTS = "SELECTED_PARTS"
    const val TECHNICIAN_ID = "SELECTED_PARTS"
    const val COMPLAINT_ID = "COMPLAINT_ID"
    const val TOTAL_ENQUIRY = "TOTAL_ENQUIRY"

    object ComingFrom {

        val CUSTOMER_DETAILS = "CUSTOMER_DETAILS"
        val DASHBOARD = "DASHBOARD"

    }

    // Menu Dash Items
    const val REQUEST_NEW = "Service_New"
    const val REQUEST_ASSIGN = "Service_Assign"
    const val REQUEST_VERIFY = "Service_Verify"
    const val CHARGEABLE_NEW = "Service_Chargeable"
    const val CHARGEABLE_COMPLETED = "Service_Completed_request"
    const val CHARGEABLE_CANCELLED = "Service_Cancelled"

    const val TECHNICIAN_REQUEST_NEW = "Technician_New_Request"

    const val NEW = "New"
    const val ASSIGN = "Assign"
    const val VERIFY = "Verify"
    const val NEW_CHARGEABLE = "New"
    const val COMPLETED_CHARGEABLE = "Completed"
    const val CANCELLED_CHARGEABLE = "Cancelled"

    const val RECEIVE_DATA = "RECEIVE_DATA"


    const val PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.orpatservice.app"
}